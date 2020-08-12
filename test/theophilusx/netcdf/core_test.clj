(ns theophilusx.netcdf.core-test
  (:require [theophilusx.netcdf.core :as sut]
            [clojure.test :refer [deftest testing is]]
            [cprop.core :refer [load-config]]
            [theophilusx.netcdf.file :refer [file-type-description]]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-files ["test.nc" "test_hgroups.nc" "testrh.nc"
                 "test_echam_spectral-deflated.nc" "OMI-Aura_L2-example.nc"
                 "access-s.nc"])

(def test-file (atom ""))

(deftest with-netcdf-test
  (testing (str "Testing with file " @test-file)
    (sut/with-netcdf [nc @test-file]
      (is (string? (file-type-description nc))))))

(deftest with-memory-netcdf-test
  (testing (str "Testing with file " @test-file)
    (sut/with-memory-netcdf [nc @test-file]
      (is (string? (file-type-description nc))))))

(deftest core-testing
  (doseq [f test-files]
    (reset! test-file (str (:test-data conf) "/" f))
    (with-netcdf-test)
    (with-memory-netcdf-test)))

(defn test-ns-hook []
  (core-testing))
