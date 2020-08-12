(ns netcdf.dimensions
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
  "Return the dimension as a map with keys for :name, :length, 
:unlimited? and :varies?"
  [^Dimension dim]
  (when dim
    {:name       (-dimension-name dim)
     :length     (-dimension-length dim)
     :unlimited? (-dimension-unlimited? dim)
     :varying?   (-dimension-varies? dim)
     :obj        dim}))

(defn -dimensions->vector [dim-list]
  (mapv #'-dimension->map dim-list))


(defn dimension->string
  ([d-map]
   (dimension->string d-map ""))
  ([d-map indent]
   (str indent "Name: " (:name d-map) " Length: " (:length d-map)
        " Unlimited? " (:unlimited? d-map)
        " Varying? " (:varying? d-map))))

(defn dimension
  "Reurn the specified dimension"
  [^NetcdfFile nc dimension-name]
  (-dimension->map (.findDimension nc dimension-name)))

(defn dimensions
  "Returns vector of maps representing the files dimensions"
  [^NetcdfFile nc]
  (-dimensions->vector (.getDimensions nc)))
