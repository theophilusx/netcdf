(ns theophilusx.netcdf.data
  (:require [theophilusx.netcdf.arrays :as arrays]))


(defn read-slice [v origin size]
  (arrays/array->map (.read (:obj v) (int-array origin) (int-array size))))

(defn read-value [v origin]
  (let [size   (take (count origin) (range 1 2 0))
        values (read-slice v origin size)]
    (arrays/get-element values 0))) 
