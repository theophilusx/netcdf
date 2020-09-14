(ns theophilusx.netcdf.core
  (:require [theophilusx.netcdf.file :as files]
            [theophilusx.netcdf.variables :as vars]
            [theophilusx.netcdf.attributes :as attrs]
            [theophilusx.netcdf.dimensions :as dims]
            [theophilusx.netcdf.groups :as grps]
            [theophilusx.netcdf.ranges :as rngs]
            [theophilusx.netcdf.data :as data]
            [theophilusx.netcdf.arrays :as arrays]))

(def open files/open)
(def open-in-memory files/open-in-memory)
(def close files/close)

(defmacro with-netcdf
  "A macro for opening and processing a NetCDF file, performing some operations,
  NetCDF object, a `file` to open and a `body` of expressions to execute."
  [[name file] & body]
  `(let [~name (files/open ~file)]
     ~@body
     (files/close ~name)))

(defmacro with-memory-netcdf
  "Same as `with-netcdf`, but loads the file into memory. Arguments are a
  `name` to refer tot he NetCDF object, a `file` specifying the file to operate
  on and a `body` of expressions to execute."
  [[name file] & body]
  `(let [~name (files/open-in-memory ~file)]
     ~@body
     (files/close ~name)))

(def variable vars/variable)
(def variables vars/variables)

(def attribute attrs/attribute)
(def global-attribute attrs/global-attribute)
(def global-attributes attrs/global-attributes)

(def dimension dims/dimension)
(def dimensions dims/dimensions)

(def root-group grps/root-group)
(def group grps/group)

(def make-range rngs/make-range)
(def make-named-range rngs/make-named-range)

(def read-value data/read-value)
(def read-slice data/read-slice)
(def read-scalar data/read-scalar)

(def get-linear-value arrays/get-linear-value)
(def get-value arrays/get-value)

