(ns netcdf.file
  (:import [ucar.nc2 NetcdfFile]
           [java.io FileNotFoundException IOException])
  ;(:require [clj-netcdf.groups :as groups])
  )

(defn open
  "Open a NetCDF file. Return a `NetcdfFile` object. `location` is a path
  to an NetCDF 4 file. The optional keyword arguement value pair
  `buffer-size`can be used to set a specific buffer size to hold the
  data. Defaults to 0, which says to use the default buffer size.

  Examples
  (open \"/path/to/data.nc\")
  (open \"/path/to/data-nc\" :buffer-size 1024000)"
  [location & {:keys [buffer-size] :or {buffer-size 0}}]
  (let [file (NetcdfFile/canonicalizeUriString location)]
    (try
      (NetcdfFile/open file buffer-size nil)
      (catch FileNotFoundException e
        (throw (Exception. (str "File " file " not found"))))
      (catch IOException e
        (throw (Exception. (str "File " file " is not a valid CDN file")))))))

(defn open-in-memory
  "Opens a NetCDF file and read it into memory. `location` is a path to the
  location of the NetCDF 4 file. Returns a `NetcdfFile` object."
  [location]
  (let [file (NetcdfFile/canonicalizeUriString location)]
    (try
      (NetcdfFile/openInMemory file)
      (catch FileNotFoundException e
        (throw (Exception. (str "File " file " not found"))))
      (catch IOException e
        (throw (Exception. (str "File " file " is not a valid CDN file")))))))

(defn close
  "Closes a NetCDF file object and releases resources. `nc` is a 
  `NetcdfFile` object returned from a call to `open` or `open-in-memory`."
  [nc]
  {:pre [(= (type nc) NetcdfFile)]}
  (.close nc)
  true)


