(ns netcdf.attributes
  (:import [ucar.nc2 NetcdfFile Attribute]
           [ucar.ma2 DataType]
           [ucar.nc2.util EscapeStrings]))

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
