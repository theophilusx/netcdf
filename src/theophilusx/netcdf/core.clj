(ns theophilusx.netcdf.core
  (:require [theophilusx.netcdf.file :as f]))

(defmacro with-netcdf
  "A macro for opening and processing a NetCDF file, performing some operations,
  NetCDF object, a `file` to open and a `body` of expressions to execute."
  [[name file] & body]
  `(let [~name (f/open ~file)]
     ~@body
     (f/close ~name)))

(defmacro with-memory-netcdf
  "Same as `with-netcdf`, but loads the file into memory. Arguments are a 
  `name` to refer tot he NetCDF object, a `file` specifying the file to operate 
  on and a `body` of expressions to execute."
  [[name file] & body]
  `(let [~name (f/open-in-memory ~file)]
     ~@body
     (f/close ~name)))


