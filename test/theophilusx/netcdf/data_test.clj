(ns theophilusx.netcdf.data-test
  (:require [theophilusx.netcdf.data :as sut]
            [theophilusx.netcdf.core :refer [with-netcdf]]
            [theophilusx.netcdf.variables :refer [variable]]
            [cprop.core :refer [load-config]]
            [clojure.test :refer [deftest testing is]]))


(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-files ["test.nc" "test_hgroups.nc" "testrh.nc"
                 "test_echam_spectral-deflated.nc" "OMI-Aura_L2-example.nc"
                 "access-s.nc"])

(def test-file (atom ""))

(deftest read-values
  (testing "Reading single value"
    (reset! test-file (str (:test-data conf) "/access-s.nc"))
    (with-netcdf [nc @test-file]
      (let [v (variable nc "tasmax")]
        (testing (str "reading " (:name v))
          (testing "0 300 400"
            (let [val (sut/read-value v [0 300 400])]
              (is (= (float 23.84) val))))
          (testing "0 300 401"
            (let [val (sut/read-value v [0 300 401])]
              (is (= (float 23.9) val))))
          (testing "0 301 400"
            (let [val (sut/read-value v [0 301 400])]
              (is (= (float 24) val))))
          (testing "0 301 401"
            (let [val (sut/read-value v [0 301 401])]
              (is (= (float 24) val))))
          (testing "1 300 400"
            (let [val (sut/read-value v [1 300 400])]
              (is (= (float 24.94) val))))
          (testing "1 300 401"
            (let [val (sut/read-value v [1 300 401])]
              (is (= (float 24.96) val))))
          (testing "1 301 400"
            (let [val (sut/read-value v [1 301 400])]
              (is (= (float 25.04) val))))
          (testing "1 301 401"
            (let [val (sut/read-value v [1 301 401])]
              (is (= (float 25.01) val)))))))))
