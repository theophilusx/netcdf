(ns netcdf.variables
  (:require [netcdf.attributes :as attributes]
            [netcdf.dimensions :as dimensions]
            [netcdf.ranges :as ranges]
            [clojure.string :as string])
  (:import [ucar.nc2 NetcdfFile Variable]
           [ucar.ma2 DataType]
           [ucar.nc2.util EscapeStrings]))

(defn -variable-attributes [variable]
  (mapv #'attributes/-attribute->map (.getAttributes variable)))

(defn -variable-type
  "Return symbol representing the variable type"
  [variable]
  (keyword (string/lower-case (str (.getDataType variable)))))

(defn -variable-description [variable]
  (.getDescription variable))

(defn -variable-dap-name [variable]
  (Variable/getDAPName variable))

(defn -variable-dimensions [variable]
  (mapv #'dimensions/-dimension->map (.getDimensions variable)))

(defn -variable-element-size [variable]
  (.getElementSize variable))

(defn -variable-ranges [variable]
  (ranges/-ranges->vector (.getRanges variable)))

(defn -variable-rank [variable]
  (.getRank variable))

(defn -variable-shape [variable]
  (vec (seq (.getShape variable))))

(defn -variable-size [variable]
  (.getSize variable))

(defn -variable->map [variable]
  (when variable
    {:description  (-variable-description variable)
     :name         (-variable-dap-name variable)
     :type         (-variable-type variable)
     :attributes   (-variable-attributes variable)
     :dimensions   (-variable-dimensions variable)
     :element-size (-variable-element-size variable)
     :ranges       (-variable-ranges variable)
     :rank         (-variable-rank variable)
     :shape        (-variable-shape variable)
     :size         (-variable-size variable)
     :obj          variable}))

(defn -variables->vector [var-list]
  (mapv #'-variable->map var-list))


(defn variable->string
  ([v-map]
   (variable->string v-map ""))
  ([v-map indent]
   (str indent (:dap-name v-map)
        " Desc: " (:description v-map)
        " " (:type v-map)
        " Size: " (:size v-map)
        " Rank: " (:rank v-map) "\n"
        indent "Shape: " (:shape v-map)
        " Element Size: " (:element-size v-map) "\n"
        indent "Ranges:\n"
        (clojure.string/join "\n"
                             (map
                              #(ranges/range->string % (str indent "\t"))
                              (:ranges v-map))) "\n"
        indent "Attributes:\n"
        (clojure.string/join "\n"
                             (map
                              #(attributes/attribute->string % (str indent "\t"))
                              (:attributes v-map))) "\n"
        indent "Dimensions:\n"
        (clojure.string/join "\n"
                             (map
                              #(dimensions/dimension->string % (str indent "\t"))
                              (:dimensions v-map))))))

(defn variable
  "Find a variable by either full name or by supplying group and short name."
  ([nc full-name]
   (-variable->map (.findVariable nc (EscapeStrings/escapeDAPIdentifier full-name))))
  ([nc group short-name]
   (-variable->map (.findVariable nc group short-name))))

(defn variables [nc]
  (-variables->vector (.getVariables nc)))
