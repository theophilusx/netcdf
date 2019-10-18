(ns netcdf.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [netcdf.core :as sut]))

(deftest default
  (testing "broken - no tests defined"
    (is (thrown? Exception (/ 1 0)))))
