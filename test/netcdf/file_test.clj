(ns netcdf.file-test
  (:import [ucar.nc2 NetcdfFile])
  (:require [clojure.test :refer [deftest testing is]]
            [netcdf.file :as sut]))

(deftest open-file
  (testing "open and close existing file"
    (let [nc (sut/open "test-data/test.nc")]
      (is (= (instance? NetcdfFile nc) true))
      (is (= (sut/close nc) true))))
  (testing "open non-existing file throws exception"
    (is (thrown? Exception (sut/open "no-such-file"))))
  (testing "close invalid nc object throws exception"
    (is (thrown? AssertionError (sut/close nil)))))

(deftest open-file-in-memory
  (testing "open in memory file and close for existing file"
    (let [nc (sut/open-in-memory "test-data/test.nc")]
      (is (= (instance? NetcdfFile nc) true))
      (is (= (sut/close nc) true))))
  (testing "open in memory non-existing file throws exception"
    (is (thrown? Exception (sut/open-in-memory "no-such-file"))))
  (testing "close invalid nc object throws exception"
    (is (thrown? AssertionError (sut/close nil)))))
