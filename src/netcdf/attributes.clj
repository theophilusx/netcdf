(ns netcdf.attributes
  (:import [ucar.nc2 NetcdfFile Attribute]
           [ucar.ma2 DataType]))

(defn attribute-name
  "Return the attribute name"
  [attr]
  (.getName attr))

(defn attribute-length
  "Return length of the attribute. Value > 1 = array"
  [attr]
  (.getLength attr))

(defn attribute-value
  "Return attribute value - string, numeric or array"
  [attr]
  (let [dt (.getDataType attr)]
    (cond
      (.isString attr)            (.getStringValue attr)
      (and (.isNumeric dt)
           (not (.isArray attr))) (.getNumericValue attr)
      :default                    (.getValues attr))))

(defn attribute-type
  "Return symbol representing the attribute type"
  [attr]
  (keyword (.toString (.getDataType attr))))

(defn attribute->map
  "Return attribute as a map with keys :name, :type, :length and :value"
  [attr]
  {:name (attribute-name attr)
   :type (attribute-type attr)
   :length (attribute-length attr)
   :value (attribute-value attr)})

(defn attributes->vector [attr-list]
  (vec (map #'attribute->map attr-list)))

(defn global-attributes
  "Return vector of global attributes as maps. `nc` is a `ucar.nc2.NetcdfFile`
  object"
  [nc]
  (attributes->vector (.getGlobalAttributes nc)))

(defn attribute->string
  ([a-map]
   (attribute->string a-map ""))
  ([a-map indent]
   (str indent (:name a-map) 
        ": " (:value a-map))))
