(ns netcdf.file
  (:import [ucar.nc2 NetcdfFile NetcdfFiles]
           [java.io FileNotFoundException IOException])
  (:require [clojure.string :as string]
            [clojure.edn :as edn]))

(defn ^NetcdfFile open
  "Open a NetCDF file. Return a `NetcdfFile` object. `location` is a path
  to an NetCDF 4 file. The optional keyword arguement value pair
  `buffer-size`can be used to set a specific buffer size to hold the
  data. Defaults to 0, which says to use the default buffer size.

  Examples
  (open \"/path/to/data.nc\")
  (open \"/path/to/data-nc\" :buffer-size 1024000)"
  [location & {:keys [buffer-size] :or {buffer-size 0}}]
  (let [file (NetcdfFiles/canonicalizeUriString location)]
    (try
      (NetcdfFiles/open file buffer-size nil)
      (catch FileNotFoundException e
        (throw (Exception. (str "File " file " not found"))))
      (catch IOException e
        (throw (Exception. (str "File " file " is not a valid CDN file")))))))

(defn ^NetcdfFile open-in-memory
  "Opens a NetCDF file and read it into memory. `location` is a path to the
  location of the NetCDF 4 file. Returns a `NetcdfFile` object."
  [location]
  (let [file (NetcdfFiles/canonicalizeUriString location)]
    (try
      (NetcdfFiles/openInMemory file)
      (catch FileNotFoundException e
        (throw (Exception. (str "File " file " not found"))))
      (catch IOException e
        (throw (Exception. (str "File " file " is not a valid CDN file")))))))

(defn close
  "Closes a NetCDF file object and releases resources. `nc` is a 
  `NetcdfFile` object returned from a call to `open` or `open-in-memory`."
  [^NetcdfFile nc]
  {:pre [(= (type nc) NetcdfFile)]}
  (.close nc)
  true)

(defn type-id
  "Get the file type id for the underlying data source. The `nc` argument is a
  `NetcdfFile` object returned from a call to `open` or `open-in-memory`"
  [^NetcdfFile nc]
  {:pre [(= (type nc) NetcdfFile)]}
  (.getFileTypeId nc))

(defn location
  "Return the location of the NetCDF file. Argument `nc` is a `NetcdfFile` object
  returned from a call to `open` or `open-in-memory`."
  [^NetcdfFile nc]
  {:pre [(= (type nc) NetcdfFile)]}
  (.getLocation nc))

(defn -mk-var-info-map [line]
  (let [[var-name start size unlim] (string/split line #" +")]
    {(keyword var-name) {:start start
                         :size size
                         :unlimited (if (= unlim "true")
                                      true
                                      false)}}))

(defn -mk-cache-info-map [line]
  (let [[var-name cached size c-size] (string/split line #" +")]
    {(keyword var-name) {:cached? (if (= cached "true")
                                    true
                                    false)
                         :size (or (edn/read-string size)
                                   0)
                         :cached-size (or (clojure.edn/read-string c-size)
                                          0)}}))

(defn -parse-iosp-tables [data]
  (let [state (reduce (fn [acc line]
               (cond
                 (or (string/index-of line "=")
                     (empty? line)) acc
                 (string/starts-with? line "name____start")
                 (assoc acc :state :var-info)
                 (string/starts-with? line "Variable  isCaching")
                 (assoc acc :state :cache-info)
                 :else
                 (if (= (:state acc) :var-info)
                   (let [var-data (-mk-var-info-map line)]
                     (assoc acc :var-info (merge (:var-info acc) var-data)))
                   (when (= (:state acc) :cache-info)
                     (let [cache-data (-mk-cache-info-map line)]
                       (assoc acc :cache-info
                              (merge (:cache-info acc) cache-data)))))))
                      {:state :unknown :var-info {} :cache-info {}} data)]
    {:var-info (:var-info state)
     :cache-info (:cache-info state)}) )

(defn -mk-iosp-var-map [data]
  (reduce (fn [acc v]
            (if (= (count v) 2)
              (assoc acc (first v) (second v))
              acc))
          {} (map (fn [line]
                    (when (and (string/index-of line "=")
                             (not (string/starts-with? line "total")))
                      (let [[name val] (string/split line #"=")]
                        [(keyword (string/replace (string/trim name) " " "-"))
                         (string/trim val)])))
                  data)))

;; (defn iosp-info
;;   "Return details of underlying ISOP information. `nc` is a `NetcdfFile` object
;;   returns from a call to `open` or `open-in-memory`. Returned value is a map."
;;   [^NetcdfFile nc]
;;   {:pre [(= (type nc) NetcdfFile)]}
;;   (let [raw (map string/trim (string/split-lines (.getDetailInfo nc)))
;;         var-list-map (-mk-iosp-var-map raw)
;;         table-map (-parse-iosp-tables raw)]
;;     (merge var-list-map table-map)))

(defn file-type-description
  "Return description of file type. `nc` is a `NetcdfFile` object returned from a
  call to `open` or `open-in-memory`. Returns a `string`"
  [^NetcdfFile nc]
  {:pre [(= (type nc) NetcdfFile)]}
  (.getFileTypeDescription nc))

(defn file-type-version
  "Get the file type version for the file. `nc` is a `NetcdfFile` object returned
  from a call to `open` or `open-in-memory`. Returns a `string`."
  [^NetcdfFile nc]
  {:pre [(= (type nc) NetcdfFile)]}
  (.getFileTypeVersion nc))

