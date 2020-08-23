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
