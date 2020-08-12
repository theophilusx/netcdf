(ns netcdf.dimensions-test
  (:require [netcdf.dimensions :as sut]
            [clojure.test :refer [deftest testing is]]
            [netcdf.core :refer [with-netcdf]]
            [cprop.core :refer [load-config]]
            [netcdf.keys-test :as key]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-files ["test.nc" "test_hgroups.nc" "testrh.nc"
                 "test_echam_spectral-deflated.nc" "OMI-Aura_L2-example.nc"
                 "access-s.nc"])

(def test-file (atom ""))

(deftest dimensions-test
  (testing (str "Testing with file " @test-file)
    (with-netcdf [nc @test-file]
      (let [dim (sut/dimensions nc)]
        (testing "returns a vector"
          (is (vector? dim)))
        (testing "returns correct dimension count"
          (is (> (count dim) 0)))
        (testing "returned vector of maps with correct keys"
          (is (every? (fn [d]
                        (every? key/dimensions (keys d)))
                      dim)))))))

(deftest dimension-test
  (testing (str "Testing with file " @test-file)
    (with-netcdf [nc @test-file]
      (let [d-names (map #(:name %) (sut/dimensions nc))
            ]
        (doseq [d d-names]
          (when-let [dim (sut/dimension nc d)]
            (testing (str "dimension " d)
              (is (every? key/dimensions (keys dim)))
              (is (= (:name dim) d))
              (is (number? (:length dim)))
              (is (boolean? (:unlimited? dim)))
              (is (boolean? (:varying? dim))))))))))

(deftest dimensions-testing
  (doseq [f test-files]
    (reset! test-file (str (:test-data conf) "/" f))
    (dimensions-test)
    (dimension-test)))

(defn test-ns-hook []
  (dimensions-testing))
