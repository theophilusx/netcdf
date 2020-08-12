(ns netcdf.file-test
  (:import [ucar.nc2 NetcdfFile])
  (:require [clojure.test :refer [deftest testing is]]
            [netcdf.file :as sut]
            [cprop.core :refer [load-config]]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-files ["test.nc" "test_hgroups.nc" "testrh.nc"
                 "test_echam_spectral-deflated.nc" "OMI-Aura_L2-example.nc"
                 "access-s.nc"])

(def test-file (atom ""))

(deftest open-non-existing-test
  (testing "Opening a non-existent file throws exception"
    (is (thrown? Exception (sut/open "no-such-file")))))

(deftest open-in-memory-non-existing-test
  (testing "open in memory non-existing file throws exception"
    (is (thrown? Exception (sut/open-in-memory "no-such-file")))))

(deftest close-non-existing-test
  (testing "Closing a non-existing object throws exception"
    (is (thrown? AssertionError (sut/close nil)))))

(deftest open-file-test
  (testing (str "open-file: Testing using file " @test-file)
    (let [nc (sut/open @test-file)]
      (is (= (instance? NetcdfFile nc) true))
      (is (= (sut/close nc) true)))))

(deftest open-file-in-memory-test
  (testing (str "open-in-memory: Testing with file " @test-file)
    (let [nc (sut/open-in-memory @test-file)]
      (is (= (instance? NetcdfFile nc) true))
      (is (= (sut/close nc) true)))))

(deftest type-id-test
  (testing (str "type-id: Testing with file " @test-file)
    (let [nc (sut/open @test-file)]
      (is (string? (sut/type-id nc)))
      (sut/close nc))))

(deftest location-test
  (testing (str "location: Testing with file " @test-file)
    (let [nc (sut/open @test-file)]
      (is (= (sut/location nc) @test-file))
      (sut/close nc))))

;; (deftest iosp-info-test
;;   (testing (str "isop-info: Testing with file " @test-file)
;;     (let [nc (sut/open @test-file)]
;;       (is (map? (sut/iosp-info nc)))
;;       (sut/close nc))))

(deftest file-type-description-test
  (testing (str "file-type-description: Testing with file " @test-file)
    (let [nc (sut/open @test-file)]
      (is (string? (sut/file-type-description nc)))
      (sut/close nc))))

(deftest file-type-version-test
  (testing (str "file-type-version: Testing with file " @test-file)
    (let [nc (sut/open @test-file)]
      (is (string? (sut/file-type-version nc)))
      (sut/close nc))))

(deftest file-testing
  (open-non-existing-test)
  (open-in-memory-non-existing-test)
  (close-non-existing-test)
  (doseq [f test-files]
    (reset! test-file (str (:test-data conf) "/" f))
    (open-file-test)
    (open-file-in-memory-test)
    (type-id-test)
    (location-test)
    ;(iosp-info-test)
    (file-type-description-test)
    (file-type-version-test)))

(defn test-ns-hook []
  (file-testing))
