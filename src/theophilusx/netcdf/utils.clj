(ns theophilusx.netcdf.utils
  (:require [clojure.string :as string]))

(defn type->keyword [t]
  (keyword (string/lower-case (str t))))
