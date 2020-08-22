(ns theophilusx.netcdf.attributes
  "Functions to manipulate `Attribute` objects."
  (:import [ucar.nc2 NetcdfFile Attribute])
  (:require [theophilusx.netcdf.utils :as utils]))

(defn -attribute-name
  "Return the attribute name"
  [^Attribute attr]
  (.getFullName attr))

(defn -attribute-length
  "Return length of the attribute. Value > 1 = array"
  [^Attribute attr]
  (.getLength attr))

(defn -attribute-value
  "Return attribute value - string, numeric or array"
  [^Attribute attr]
  (let [dt (.getDataType attr)]
    (cond
      (.isString attr)            (.getStringValue attr)
      (and (.isNumeric dt)
           (not (.isArray attr))) (.getNumericValue attr)
      :else                    (.getValues attr))))

(defn -attribute-type
  "Return symbol representing the attribute type"
  [^Attribute attr]
  (utils/type->keyword (.getDataType attr)))

(defn -attribute->map
  "Return an attribute as a map with the following keys

  | Key       | Description                          |
  |-----------|--------------------------------------|
  | `:name`   | The attribute name                   |
  | `:type`   | The attribute data type as a keyword |
  | `:length` | The attribute length                 |
  | `:value`  | The attribute value                  |"
  [^Attribute attr]
  (when attr
    {:name   (-attribute-name attr)
     :type   (-attribute-type attr)
     :length (-attribute-length attr)
     :value  (-attribute-value attr)
     :obj attr}))

(defn -attributes->vector
  "Returns a `vector` of attribute maps. The `attr-list` argument is a
  collection of Java `Attribute` objects."
  [attr-list]
  (mapv #'-attribute->map attr-list))


(defn attribute->string
  "Return a `string` representation of an attribute."
  ([a-map]
   (attribute->string a-map ""))
  ([a-map indent]
   (str indent (:name a-map) 
        ": " (:value a-map))))

(defn attribute
  "Find an attribute given the full attribute name. `nc` is a
  `ucar.nc2.NetcdfFile` object. `attr-name` is a full attribute name. The
  attribute may be nested in multiple groups and/or structures. A `.` is used to
  separate structures, a `/` is prefixed to grups and an `@` is prefixed to
  attributes. e.g.
  /group/variable@attribute
  /group/variable/structure.member@attribute
  Returns `ucar.mc2.Attribute` if attribute is found, `nil` otherwise."
  [^NetcdfFile nc attr-name]
  (-attribute->map (.findAttribute nc attr-name)))

(defn global-attribute
  "Return a global attribute as an attribute map. The `nc` argument is a
  `ucar.nc2.NetcdfFile` object and `attr-name` is a case sensitive attribute
  name."
  [^NetcdfFile nc attr-name]
  (-attribute->map (.findGlobalAttribute nc attr-name)))

(defn global-attributes
  "Return a `vector` of global attributes as attribute maps. `nc` is a
  `ucar.nc2.NetcdfFile` object returns from call to `open`,
  `open-file-in-memory`, `with-netcdf` or `with-memory-netecdf`."
  [^NetcdfFile nc]
  (-attributes->vector (.attributes (.getRootGroup nc))))

