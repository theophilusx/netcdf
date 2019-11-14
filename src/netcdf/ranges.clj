(ns netcdf.ranges
  (:import [ucar.ma2 Range]))

(defn -range-name [range]
  (.getName range))

(defn -range-first [range]
  (.first range))

(defn -range-last [range]
  (.last range))

(defn -range-length [range]
  (.length range))

(defn -range-stride [range]
  (.stride range))

(defn -range->map
  "Converts a Netcdf range object into a map with keys for `name`, `first`,
  `last`, `length`, `stride` and `obj`. The `name` value may be nil and `obj`
  contains the original Netcdf range object"
  [range]
  (when range
    {:name   (-range-name range)
     :first  (-range-first range)
     :last   (-range-last range)
     :length (-range-length range)
     :stride (-range-stride range)
     :obj    range}))

(defn -ranges->vector
  "Takes a list of Netcdf range objects and converts it to a vector of range
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
  "Creates a new range object. Returns a range map with keys `name`, `first`,
  `last`, `length` and `stride`."
  ([len]
   (-range->map (Range. len)))
  ([start end]
   (-range->map (Range. start end)))
  ([start end stride]
   (-range->map (Range. start end stride)))
  ([rname start end stride]
   (-range->map (Range. rname start end stride))))

