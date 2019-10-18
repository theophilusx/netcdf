(ns netcdf.file-test
  (:import [ucar.nc2 NetcdfFile])
  (:require [clojure.test :refer [deftest testing is]]
            [theophilusx.file :as sut]))

(deftest open-file
  (testing "open and close existing file"
    (let [nc (sut/open "test-data/test.nc")]
      (is (= (instance? NetcdfFile nc) true))
      (is (= (sut/close nc) true))))
  (testing "open non-existing file throws exception"
    (is (thrown? Exception (sut/open "no-such-file"))))
  (testing "close invalid nc object throws exception"
    (is (thrown? Exception (sut/close nil)))))
