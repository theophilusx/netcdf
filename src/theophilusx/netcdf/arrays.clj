(ns theophilusx.netcdf.arrays
  (:require [theophilusx.netcdf.utils :as utils])
  (:import [ucar.ma2 Array]))

(defn array->map [^Array a]
  {:type        (utils/type->keyword (.getDataType a))
   :elementType (utils/type->keyword (.getElementType a))
   :rank        (.getRank a)
   :shape       (vec (.getShape a))
   :size        (.getSize a)
   :obj         a})

(defn get-element [a index]
  (condp = (:type a)
    :boolean (.getBoolean (:obj a) index)
    :byte    (.getByte (:obj a) index)
    :char    (.getChar (:obj a) index)
    :double  (.getDouble (:obj a) index)
    :float   (.getFloat (:obj a) index)
    :int     (.getInt (:obj a) index)
    :long    (.getLong (:obj a) index)
    :object  (.getObject (:obj a) index)
    :short   (.getShort (:obj a) index)
    :string  (.getString (:obj a) index)
    (throw (Exception. (str "Unknown type " (:type a) " for array")))))

(defn get-value
  ([a]
   (.get (:obj a)))
  ([a i]
   (.get (:obj a) i))
  ([a i0 i1]
   (.get (:obj a) i0 i1))
  ([a i0 i1 i2]
   (.get (:obj a) i0 i1 i2))
  ([a i0 i1 i2 i3]
   (.get (:obj a) i0 i1 i2 i3))
  ([a i0 i1 i2 i3 i4]
   (.get (:obj a) i0 i1 i2 i3 i4))
  ([a i0 i1 i2 i3 i4 i5]
   (.get (:obj a) i0 i1 i2 i3 i4 i5))
  ([a i0 i1 i2 i3 i4 i5 i6]
   (.get (:obj a) i0 i1 i2 i3 i4 i5 i6)))
