(ns theophilusx.netcdf.variables
  "Functions for manipulating `Variable` objects."
  (:require [theophilusx.netcdf.attributes :as attributes]
            [theophilusx.netcdf.dimensions :as dimensions]
            [theophilusx.netcdf.ranges :as ranges]
            [clojure.string :as string])
  (:import [ucar.nc2 NetcdfFile Group Variable]
           [ucar.nc2.util EscapeStrings]))

(defn -variable-attributes
  "Extract a vector of attribute map data from a variable. See
  `theophilusx.netcdf.attributes` for description of available map keys."
  [^Variable v]
  (mapv #'attributes/-attribute->map (.attributes v)))

(defn -variable-type
  "Return symbol representing the variable data type"
  [^Variable v]
  (keyword (string/lower-case (str (.getDataType v)))))

(defn -variable-description
  "Return a string describing the variable."
  [^Variable v]
  (.getDescription v))

(defn -variable-dap-name
  "Return string name of variable."
  [^Variable v]
  (Variable/getDAPName v))

(defn -variable-dimensions
  "Returns a `map` representing the dimensions for a variable. See
  `theophilusx.netcdf.dimensions` for a description of the map keys."
  [^Variable v]
  (mapv #'dimensions/-dimension->map (.getDimensions v)))

(defn -variable-element-size
  "Returns the size of variable elements in bytes."
  [^Variable v]
  (.getElementSize v))

(defn -variable-ranges
  "Return a vector of range maps describing the variable ranges. See
  `theophilusx.netcdf.ranges` for a description of available map keys."
  [^Variable v]
  (ranges/-ranges->vector (.getRanges v)))

(defn -variable-rank
  "Return the number of dimensions (rank) for a variable." 
  [^Variable v]
  (.getRank v))

(defn -variable-shape
  "Return a vector containing the length of each dimension in the variable."
  [^Variable v]
  (vec (seq (.getShape v))))

(defn -variable-size
  "Return the total number of elements in the variable."
  [^Variable v]
  (.getSize v))

(defn -variable-coordinate?
  "True if the variable is a coordinate variable, false otherwise."
  [^Variable v]
  (.isCoordinateVariable v))

(defn -variable-metadata?
  "True if the variable is a metadata variable."
  [^Variable v]
  (.isMetadata v))

(defn -variable-scalar?
  "True if this is a scalar variable."
  [^Variable v]
  (.isScalar v))

(defn -variable-unlimited?
  "True if this variable has an unlimited dimension."
  [^Variable v]
  (.isUnlimited v))

(defn -variable-variable-length?
  "True if this variable has a variable length dimension."
  [^Variable v]
  (.isVariableLength v))

(defn -variable->map
  "A `map` which describes a NetCDF variable. Available keys are

  | key                   | Description                                       |
  |-----------------------|---------------------------------------------------|
  | `:description`        | A description of the variable                     |
  | `:name`               | The DAP name of the variable                      |
  | `:type`               | The data type of the variable as a keyword        |
  | `:attributes`         | A vector of attribute maps describing attributes  |
  |                       | associated with the variable.                     |
  | `:dimensions`         | A vector of dimension maps describing the         |
  |                       | dimensions of the variable.                       |
  | `:element-size`       | The byte size for elements in this variable.      |
  | `:ranges`             | A vector of range maps describing the ranges      |
  |                       | for each dimension of this variable.              |
  | `:rank`               | The number of dimensions for this variable.       |
  | `:shape`              | A vector containing the length for each dimension |
  |                       | in this variable.                                 |
  | `:size`               | Total number of elements in this variable.        |
  | `:is-coordinate?`     | True if this variable is a coordinate variable.   |
  | `:is-metadata?`       | True if this variable is metadata                 |
  | `:is-scalar?`         | True if this variable is a scalar variable.       |
  | `:is-unlimited?`      | True if this variable has an unlimited size       |
  |                       | dimension.                                        |
  | `:is-variable-length` | True if this variable has a variable length       |
  |                       | dimension.                                        |
  | `:obj`                | The low level raw Java `Variable` object          |"
  [^Variable v]
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

(defn -variables->vector
  "Returns a vector of variable maps. The `var-list` is a collection of Java
  `Variable` objects."
  [var-list]
  (mapv #'-variable->map var-list))

(defn variable->string
  "Return a string representation of the variable details extracted from a
  variable `map`. "
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
  "Find a variable by either full name or by supplying group and short name.
  Returns a variable map."
  ([^NetcdfFile nc var-name]
   (if (clojure.string/starts-with? var-name "/")
     (-variable->map
      (.findVariable nc (EscapeStrings/escapeDAPIdentifier var-name)))
     (variable nc (.getRootGroup nc) var-name)))
  ([^NetcdfFile nc ^Group group var-name]
   (-variable->map
    (.findVariable nc group (EscapeStrings/escapeDAPIdentifier var-name)))))

(defn variables
  "Returns a vector of variable maps representing all the variables defined
  in a NetCEF file."
  [^NetcdfFile nc]
  (-variables->vector (.getVariables nc)))
