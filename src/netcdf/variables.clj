(ns netcdf.variables
  (:require [netcdf.attributes :as attributes]
            [netcdf.dimensions :as dimensions]
            [netcdf.ranges :as ranges]
            [clojure.string :as string])
  (:import [ucar.nc2 NetcdfFile Group Variable]
           [ucar.nc2.util EscapeStrings]))

(defn -variable-attributes [^Variable variable]
  (mapv #'attributes/-attribute->map (.getAttributes variable)))

(defn -variable-type
  "Return symbol representing the variable type"
  [^Variable variable]
  (keyword (string/lower-case (str (.getDataType variable)))))

(defn -variable-description [^Variable variable]
  (.getDescription variable))

(defn -variable-dap-name [^Variable variable]
  (Variable/getDAPName variable))

(defn -variable-dimensions [^Variable variable]
  (mapv #'dimensions/-dimension->map (.getDimensions variable)))

(defn -variable-element-size [^Variable variable]
  (.getElementSize variable))

(defn -variable-ranges [^Variable variable]
  (ranges/-ranges->vector (.getRanges variable)))

(defn -variable-rank [^Variable variable]
  (.getRank variable))

(defn -variable-shape [^Variable variable]
  (vec (seq (.getShape variable))))

(defn -variable-size [^Variable variable]
  (.getSize variable))

(defn -variable->map [^Variable variable]
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
  ([^NetcdfFile nc var-name]
   (if (clojure.string/starts-with? var-name "/")
     (-variable->map (.findVariable nc (EscapeStrings/escapeDAPIdentifier var-name)))
     (variable nc (.getRootGroup nc) var-name)))
  ([^NetcdfFile nc ^Group group var-name]
   (-variable->map (.findVariable nc group (EscapeStrings/escapeDAPIdentifier var-name)))))

(defn variables [^NetcdfFile nc]
  (-variables->vector (.getVariables nc)))
