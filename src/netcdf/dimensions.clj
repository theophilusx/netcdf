(ns netcdf.dimensions
  (:import [ucar.nc2 NetcdfFile Dimension]))

(defn dimension-name
  "Return dimension name"
  [dim]
  (.getName dim))

(defn dimension-length
  "Return length of a dimension"
  [dim]
  (.getLength dim))

(defn dimension-unlimited?
  "Return true if dimension is unlimited"
  [dim]
  (.isUnlimited dim))

(defn dimension-varies?
  "Returns true if dimension length can be varied"
  [dim]
  (.isVariableLength dim))

(defn dimension->map
  "Return the dimension as a map with keys for :name, :length, 
:unlimited? and :varies?"
  [dim]
  {:name (dimension-name dim)
   :length (dimension-length dim)
   :unlimited? (dimension-unlimited? dim)
   :varying? (dimension-varies? dim)
   :obj dim})

(defn dimensions->vector [dim-list]
  (vec (map #'dimension->map dim-list)))

(defn dimensions
  "Returns sequence of maps representing the files dimensions"
  [nc]
  (dimensions->vector (.getDimensions nc)))

(defn dimension->string
  ([d-map]
   (dimension->string d-map ""))
  ([d-map indent]
   (str indent "Name: " (:name d-map) " Length: " (:length d-map)
        " Unlimited? " (:unlimited? d-map)
        " Varying? " (:varying? d-map))))

(defn dimension
  "Reurn the specified dimension"
  [nc dimension-name]
  (.findDimension nc dimension-name))

