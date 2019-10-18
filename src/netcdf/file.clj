(ns netcdf.file
  (:import [ucar.nc2 NetcdfFile])
  ;(:require [clj-netcdf.groups :as groups])
  )

(defn open
  "Open a NetCDF file, return a `NetcdfFile` object. `location` is a path to
  a NetCDF 4 file"
  [location]
  (let [file (NetcdfFile/canonicalizeUriString location)]
    (if (NetcdfFile/canOpen file)
      (NetcdfFile/open file)
      (throw (Exception. (str "Cannot open NetCDF file " file))))))

(defn close
  "Close a `NetcdfFile` object. `nc` is the `NetcdfFile` object to close"
  [nc]
  (if (instance? NetcdfFile nc)
    (do
      (.close nc)
      true)
    (throw (Exception. "close error: not a valid NetcdfFile object"))))

