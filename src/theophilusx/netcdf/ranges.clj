(ns theophilusx.netcdf.ranges
  "Manipulate `ucar.ma2.Range` objects."
  (:import [ucar.ma2 Range]))

(defn -range-name
  "Returns the name of a range for named ranges. Un-named ranges return nil."
  [^Range range]
  (.getName range))

(defn -range-first
  "Return index of first element in the range."
  [^Range range]
  (.first range))

(defn -range-last
  "Returns index of last element in the range."
  [^Range range]
  (.last range))

(defn -range-length
  "Returns length of the range."
  [^Range range]
  (.length range))

(defn -range-stride
  "Returns stride (step size) of the range."
  [^Range range]
  (.stride range))

(defn -range->map
  "Converts a Netcdf Range object into a map with the following keys

  | Key       | Description                                              |
  |-----------|----------------------------------------------------------|
  | `:name`   | For named ranges, the name of the range. Nil for unnamed |
  |           | ranges.                                                  |
  | `:first`  | Index of first element in range.                         |
  | `:last`   | Index of last element in the range.                      |
  | `:length` | Total length of the range.                               |
  | `:stride` | Array stride (step) size                                 |
  | `:obj`    | Contains the original Java `Range` object                |"
  [^Range range]
  (when range
    {:name   (-range-name range)
     :first  (-range-first range)
     :last   (-range-last range)
     :length (-range-length range)
     :stride (-range-stride range)
     :obj    range}))

(defn -ranges->vector
  "Takes a list of `Range` objects and converts them to a vector of range
  maps"
  [range-list]
  (mapv #'-range->map range-list))

(defn range->string
  "Converts a range map to a string suitable for display."
  ([r-map]
   (range->string r-map ""))
  ([r-map indent]
   (str indent "Name: " (:name r-map)
        " First: " (:first r-map)
        " Last: " (:last r-map)
        " Length: " (:length r-map)
        " Stride: " (:stride r-map))))

(defn make-range
  "Creates a new `Range` object. Returns a range `map` with the `Range` object
  in the `:obj` key of the `map`."
  ([len]
   (-range->map (Range. len)))
  ([start end]
   (-range->map (Range. start end)))
  ([start end stride]
   (-range->map (Range. start end stride))))

(defn make-named-range
  "Create a named `Range` object. Returns a range `map` with the `Range` object
  in the `:obj` key."
  ([^String name start end]
   (-range->map (Range. name start end)))
  ([^String name start end stride]
   (-range->map (Range. name start end stride))))
