(ns netcdf.ranges
  (:import [ucar.ma2 Range]))

(defn range-name [range]
  (.getName range))

(defn range-first [range]
  (.first range))

(defn range-last [range]
  (.last range))

(defn range-length [range]
  (.length range))

(defn range-stride [range]
  (.stride range))

(defn range->map [range]
  {:name (range-name range)
   :first (range-first range)
   :last (range-last range)
   :length (range-length range)
   :stride (range-stride range)
   :obj range})

(defn ranges->vector [range-list]
  (vec (map #'range->map range-list)))

(defn range->string
  ([r-map]
   (range->string r-map ""))
  ([r-map indent]
   (str indent "Name: " (:name r-map)
        " First: " (:first r-map)
        " Last: " (:last r-map)
        " Length: " (:length r-map)
        " Stride: " (:stride r-map))))

(defn make-range
  ([len]
   (Range. len))
  ([start end]
   (Range. start end))
  ([start end stride]
   (Range. start end stride))
  ([rname start end stride]
   (Range. rname start end stride)))

