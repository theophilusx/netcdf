(ns netcdf.file-test
  (:import [ucar.nc2 NetcdfFile])
  (:require [clojure.test :refer [deftest testing is]]
            [netcdf.file :as sut]
            [cprop.core :refer [load-config]]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-file (str (:test-data conf) "/test.nc"))

(deftest open-file
  (testing "open and close existing file"
    (let [nc (sut/open test-file)]
      (is (= (instance? NetcdfFile nc) true))
      (is (= (sut/close nc) true))))
  (testing "open non-existing file throws exception"
    (is (thrown? Exception (sut/open "no-such-file"))))
  (testing "close invalid nc object throws exception"
    (is (thrown? AssertionError (sut/close nil)))))

(deftest open-file-in-memory
  (testing "open in memory file and close for existing file"
    (let [nc (sut/open-in-memory test-file)]
      (is (= (instance? NetcdfFile nc) true))
      (is (= (sut/close nc) true))))
  (testing "open in memory non-existing file throws exception"
    (is (thrown? Exception (sut/open-in-memory "no-such-file"))))
  (testing "close invalid nc object throws exception"
    (is (thrown? AssertionError (sut/close nil)))))

(deftest can-open-file
  (testing "Can open valid file returns true"
    (is (= (sut/can-open? test-file) true)))
  (testing "Can open returns false for non-existent file"
    (is (= (sut/can-open? "no-such-file.nc") false))))

(deftest get-type-id
  (testing "Can get file type id from existing file"
    (let [nc (sut/open test-file)]
      (is (= (sut/type-id nc) "NetCDF"))
      (sut/close nc)))
  (testing "type-id throws exception for bad nc"
    (is (thrown? AssertionError (sut/type-id nil)))))

(deftest get-location
  (testing "can get location of nc object"
    (let [nc (sut/open test-file)]
      (is (= (sut/location nc) test-file))
      (sut/close nc)))
  (testing "location throws assertion error for bad nc"
    (is (thrown? AssertionError (sut/location nil)))))

(deftest get-iosp-info
  (testing "return map of IOSP info for valid nc"
    (let [nc (sut/open test-file)]
      (is (map? (sut/iosp-info nc)))
      (sut/close nc)))
  (testing "throws assertion error for invalid nc"
    (is (thrown? AssertionError (sut/iosp-info nil)))))

(deftest get-file-type-description
  (testing "returns string for valid nc"
    (let [nc (sut/open test-file)]
      (is (= (sut/file-type-description nc) "NetCDF-3/CDM"))
      (sut/close nc)))
  (testing "thows assertion error for invalid nc"
    (is (thrown? AssertionError (sut/file-type-description nil)))))

(deftest get-file-type-version
  (testing "returns string representing file type version"
    (let [nc (sut/open test-file)]
      (is (= (sut/file-type-version nc) "N/A"))
      (sut/close nc)))
  (testing "thorws assertion error for bad nc"
    (is (thrown? AssertionError (sut/file-type-description nil)))))

(deftest with-netcdf-test
  (testing "with-netcdf file reads file type details"
    (sut/with-netcdf [fobj test-file]
      (is (= (sut/file-type-description fobj) "NetCDF-3/CDM")))))

(deftest with-memory-netcdf-test
  (testing "with-memory-netcdf executes"
    (sut/with-memory-netcdf [f test-file]
      (is (= (sut/file-type-description f) "NetCDF-3/CDM")))))
