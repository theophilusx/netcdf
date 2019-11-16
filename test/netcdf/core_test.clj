(ns netcdf.core-test
  (:require [netcdf.core :as sut]
            [clojure.test :refer [deftest testing is]]
            [cprop.core :refer [load-config]]
            [netcdf.file :refer [file-type-description]]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-files ["test.nc" "test_hgroups.nc" "testrh.nc"
                 "test_echam_spectral-deflated.nc" "OMI-Aura_L2-example.nc"
                 "access-s.nc"])

(def test-file (atom ""))

(deftest with-netcdf-test
  (testing (str "Testing with file " @test-file)
    (sut/with-netcdf [fobj @test-file]
      (is (string? (file-type-description fobj))))))

(deftest with-memory-netcdf-test
  (testing (str "Testing with file " @test-file)
    (sut/with-memory-netcdf [f @test-file]
      (is (string? (file-type-description f))))))

(deftest core-testing
  (doseq [f test-files]
    (reset! test-file (str (:test-data conf) "/" f))
    (with-netcdf-test)
    (with-memory-netcdf-test)))

(defn test-ns-hook []
  (core-testing))
