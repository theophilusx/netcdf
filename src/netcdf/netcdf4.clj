(ns clj-netcdf.netcdf4
  (:import [ucar.nc2 NetcdfFile]
           [ucar.nc2.util EscapeStrings]
           [java.io FileNotFoundException IOException])
  (:require [clj-netcdf.groups :as groups]
            [clojure.string :as string]))

;; file open/close/test

(defn open
      "Open a NetCDF file. Return a `ucar.nc2.NetcdfFile` object. `file-name` is a path
  to an NetCDF 4 file. The optional keyword arguement value pair
  `buffer-size`can be used to set a specific buffer size to hold the
  data. Defaults to 0, which says to use the default buffer size.

  Examples
  (open \"/path/to/data.nc\")
  (open \"/path/to/data-nc\" :buffer-size 1024000)"
      [file-name & {:keys [buffer-size] :or {buffer-size 0}}]
      (try
      (NetcdfFile/open file-name buffer-size nil)
      (catch FileNotFoundException e
        (throw (Exception. (str "File " file-name " not found"))))
      (catch IOException e
        (throw (Exception. (str "File " file-name " is not a valid CDN file"))))))


(defn open-in-memory
  "Opens a NetCDF file and read it into memory. `file-name` is a path to the
  location of the NetCDF 4 file. Returns a nc file object."
  [file-name]
  (try
    (NetcdfFile/openInMemory file-name)
    (catch FileNotFoundException e
      (throw (Exception. (str "File " file-name " not found"))))
    (catch IOException e
      (throw (Exception. (str "File " file-name " is not a valid CDN file"))))))

(defn close
  "Closes a NetCDF file object and releases resources. `nc` is a 
  Netcdf file object returned from a call to `open` or `open-in-memory`."
  [nc]
  {:pre [(= (type nc) NetcdfFile)]}
  (.close nc))

(defn can-open?
  "Test to see if we can open a NeetCDF file without actually opening it."
  [file-name]
  (try
    (NetcdfFile/canOpen file-name)
    (catch Exception e
      false)))

;; file description variables

(defn type-id
  "Get the file type id for the underlying data source. The
  `nc` argument is a netcdf object returned from a call to `open` or 
  `open-in-memory`"
  [nc]
  (.getFileTypeId nc))

(defn location
  "Return the location of the NetCDF file. Argument `nc` is a
  `ucar.nc2.NetcdfFile` object returned from a call to `open` or
  `open-in-memory`."
  [nc]
  (.getLocation nc))

(defn -mk-cache-map [l]
  (let [[var-name cached size c-size] (string/split (string/trim l) #" +")]
    {(keyword var-name) {:cached? (if (= cached "true")
                                    true
                                    false)
                         :size (or (clojure.edn/read-string size)
                                   0)
                         :cached-size (or (clojure.edn/read-string c-size)
                                          0)}}))

(defn iosp-info
  "Return details of underlying ISOP information."
  [nc]
  (let [data (map (fn [l]
                    (if (string/index-of l "=")
                      (let [[n v] (string/split l #"=")]
                        [(keyword (string/replace (string/trim n) " " "-")) (string/trim v)])
                      (if (not (empty? l))
                        [l])))
                  (string/split-lines (.getDetailInfo nc)))]
    (reduce (fn [acc v]
              (condp = (count v)
                0 acc
                1 (if (or (string/starts-with? (first v) "Variable  isCaching")
                          (string/ends-with? (first v) "-------"))
                    acc
                    (let [cache-data (-mk-cache-map (first v))]
                      (if (contains? acc :cache-info)
                        (assoc acc :cache-info (merge (:cache-info acc) cache-data))
                        (assoc acc :cache-info cache-data))))
                2 (assoc acc (first v) (second v))
                (assoc acc :unexpected-value v)))
            {} data)))

(defn file-type-description
      "Return description of file type."
      [nc]
      (.getFileTypeDescription nc))

(defn file-type-version
  "Get the file type version for the file."
  [nc]
  (.getFileTypeVersion nc))

;; group functions

(defn root-group
  "Returns a map for the root group containing keys for `:name`, `:root?`, 
  `:attributes`, `:dimensions`, `:variables`, `:obj` and `:children`. The 
  `nc` argument is a netcdf file object returned from a call to `open` or 
  `open-in-memory`. "  
  [nc]
  (groups/group->map (.getRootGroup nc)))

(defn group
  "return a group given the full group name."
  [nc group-name]
  (groups/group->map (.findGroup nc group-name)))

;; variable functions

(defn variable
  "Find a variable by either full name or by supplying group and short name."
  ([nc full-name]
   (.findVariable nc (EscapeStrings/escapeDAPIdentifier full-name)))
  ([nc group short-name]
   (.findVariable nc group short-name)))

;; dimension functions

(defn dimension
  "Reurn the specified dimension"
  [nc dimension-name]
  (.findDimension nc dimension-name))

(defn dimensions
  "Get the lsit of shared dimensions for the file."
  [nc]
  (.getDimensions nc))

;; attribute functions

(defn attribute
  "Find an attribute given the full attribute name. `nc` is a
  `ucar.nc2.NetcdfFile` object. `attr-name` is a string. Returns
  `ucar.mc2.Attribute`."
  [nc attr-name]
  (.findAttribute nc (EscapeStrings/escapeDAPIdentifier attr-name)))

(defn global-attribute
  "Return a global attribute."
  [nc attr-name]
  (.findGlobalAttribute nc attr-name))

(defn get-attribute-value-ignore-case
  "Find a global or variable attribute value. If not found, return default
  value. If variable is specified, look only for an attributre in that
  variable."
  ([nc attr-name default-val]
   (get-attribute-value-ignore-case nc nil attr-name default-val))
  ([nc var-name attr-name default-val]
   (.findAttValueIgnoreCase nc var-name attr-name default-val)))

;; helper macros

(defmacro with-netcdf
  "A macro for opening and processing a NetCDF file, performing some operations, 
  and then closing the file. Arguments are a `name` to refer to the opened NetCDF
  object, a `file` to open and a `body` of expressions to execute."
  [[name file] & body]
  `(let [~name (open ~file)]
     ~@body
     (close ~name)))

(defmacro with-memory-netcdf
  "Same as `with-netcdf`, but loads the file into memory. Arguments are a 
  `name` to refer tot he NetCDF object, a `file` specifying the file to operate 
  on and a `body` of expressions to execute."
  [[name file] & body]
  `(let [~name (open-in-memory ~file)]
    ~@body
    (close ~name)))
