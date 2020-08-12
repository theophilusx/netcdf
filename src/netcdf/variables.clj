(ns netcdf.variables
  (:require [netcdf.attributes :as attributes]
            [netcdf.dimensions :as dimensions]
            [netcdf.ranges :as ranges]
            [clojure.string :as string])
  (:import [ucar.nc2 NetcdfFile Group Variable]
           [ucar.nc2.util EscapeStrings]))

(defn -variable-attributes [^Variable v]
  (mapv #'attributes/-attribute->map (.attributes v)))

(defn -variable-type
  "Return symbol representing the variable type"
  [^Variable v]
  (keyword (string/lower-case (str (.getDataType v)))))

(defn -variable-description [^Variable v]
  (.getDescription v))

(defn -variable-dap-name [^Variable v]
  (Variable/getDAPName v))

(defn -variable-dimensions [^Variable v]
  (mapv #'dimensions/-dimension->map (.getDimensions v)))

(defn -variable-element-size [^Variable v]
  (.getElementSize v))

(defn -variable-ranges [^Variable v]
  (ranges/-ranges->vector (.getRanges v)))

(defn -variable-rank [^Variable v]
  (.getRank v))

(defn -variable-shape [^Variable v]
  (vec (seq (.getShape v))))

(defn -variable-size [^Variable v]
  (.getSize v))

(defn -variable-coordinate? [^Variable v]
  (.isCoordinateVariable v))

(defn -variable-metadata? [^Variable v]
  (.isMetadata v))

(defn -variable-scalar? [^Variable v]
  (.isScalar v))

(defn -variable-unlimited? [^Variable v]
  (.isUnlimited v))

(defn -variable-variable-length? [^Variable v]
  (.isVariableLength v))

(defn -variable->map [^Variable v]
  (when v
    {:description         (-variable-description v)
     :name                (-variable-dap-name v)
     :type                (-variable-type v)
     :attributes          (-variable-attributes v)
     :dimensions          (-variable-dimensions v)
     :element-size        (-variable-element-size v)
     :ranges              (-variable-ranges v)
     :rank                (-variable-rank v)
     :shape               (-variable-shape v)
     :size                (-variable-size v)
     :is-coordinate?      (-variable-coordinate? v)
     :is-metadata?        (-variable-metadata? v)
     :is-scalar?          (-variable-scalar? v)
     :is-unlimited?       (-variable-unlimited? v)
     :is-variable-length? (-variable-variable-length? v)
     :obj                 v}))

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
        " Coordinate?: " (:is-coordinate? v-map)
        " Metadata?: " (:is-metadata? v-map)
        " Scalar?: " (:is-scalar? v-map)
        " Unlimited?: " (:is-unlimited? v-map)
        " Variable Length?: " (:is-variable-length? v-map) "\n"
        indent "Shape: " (:shape v-map)
        " Element Size: " (:element-size v-map) "\n"
        indent "Ranges:\n"
        (string/join "\n"
                     (map
                      #(ranges/range->string % (str indent "\t"))
                      (:ranges v-map))) "\n"
        indent "Attributes:\n"
        (string/join "\n"
                     (map
                      #(attributes/attribute->string % (str indent "\t"))
                      (:attributes v-map))) "\n"
        indent "Dimensions:\n"
        (string/join "\n"
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
