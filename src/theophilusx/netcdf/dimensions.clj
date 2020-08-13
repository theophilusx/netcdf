(ns theophilusx.netcdf.dimensions
  (:import [ucar.nc2 NetcdfFile Dimension]))

(defn -dimension-name
  "Return dimension name"
  [^Dimension dim]
  (.getFullName dim))

(defn -dimension-length
  "Return length of a dimension"
  [^Dimension dim]
  (.getLength dim))

(defn -dimension-unlimited?
  "Return true if dimension is unlimited"
  [^Dimension dim]
  (.isUnlimited dim))

(defn -dimension-varies?
  "Returns true if dimension length can be varied"
  [^Dimension dim]
  (.isVariableLength dim))

(defn -dimension->map
  "Return the dimension as a map with the following keys

  | Key           | Description                          |
  |---------------|--------------------------------------|
  | `:name`       | Dimension name                       |
  | `:length`     | Dimension length                     |
  | `:unlimited?` | True if dimension is unlimited       |
  | `:varies?`    | True is dimension is variable length |"
  [^Dimension dim]
  (when dim
    {:name       (-dimension-name dim)
     :length     (-dimension-length dim)
     :unlimited? (-dimension-unlimited? dim)
     :varying?   (-dimension-varies? dim)
     :obj        dim}))

(defn -dimensions->vector
  "Return a vector of dimension maps. The `dim-list` argument is a collection
  of Java `Dimension` objects."
  [dim-list]
  (mapv #'-dimension->map dim-list))


(defn dimension->string
  "Return a dimension map as a string."
  ([d-map]
   (dimension->string d-map ""))
  ([d-map indent]
   (str indent "Name: " (:name d-map) " Length: " (:length d-map)
        " Unlimited? " (:unlimited? d-map)
        " Varying? " (:varying? d-map))))

(defn dimension
  "Reurn the specified dimension. The `nc` argument is a `NetcdfFile` object
  and dimension-name is the `string` name of a dimension."
  [^NetcdfFile nc dimension-name]
  (-dimension->map (.findDimension nc dimension-name)))

(defn dimensions
  "Returns a vector of dimension maps representing the dimensions in the
  `NetcdfFile` object."
  [^NetcdfFile nc]
  (-dimensions->vector (.getDimensions nc)))
