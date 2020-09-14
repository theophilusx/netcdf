(ns theophilusx.netcdf.variables-test
  (:require [clojure.test :refer [deftest testing is]]
            [cprop.core :refer [load-config]]
            [theophilusx.netcdf.core :refer [with-netcdf]]
            [theophilusx.netcdf.keys-test :as key]
            [theophilusx.netcdf.variables :as sut]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-files ["test.nc" "test_hgroups.nc" "testrh.nc"
                 "test_echam_spectral-deflated.nc" "OMI-Aura_L2-example.nc"
                 "access-s.nc"])

(def test-file (atom ""))

(deftest variables-test
  (testing (str "Test with file " @test-file)
    (with-netcdf [nc @test-file]
      (let [v (sut/variables nc)]
        (testing "variables returns a vector"
          (is (vector? v)))
        (testing "return vector consists of maps with correct keys"
          (is (every? (fn [vm]
                        (every? key/variables (keys vm)))
                      v)))))))

(deftest variable-test
  (testing (str "Test with file " @test-file)
    (with-netcdf [nc @test-file]
      (let [gv (sut/variables nc)
            v-names (map #(:name %) gv)]
        (doseq [v v-names]
          (let [vm (sut/variable nc v)]
            (testing (str "variable " v " is not nil")
              (is (not (nil? v))))
            (testing (str "variable " v " has correct keys")
              (is (every? key/variables (keys vm))))
            (testing (str "variable " v " description is a string or nil")
              (is (or (nil? (:description vm))
                      (string? (:description vm)))))
            (testing (str "variable " v " element-size >= 1")
              (is (>= (:element-size vm) 1)))
            (testing (str "variable " v " name matches")
              (is (= (:name vm) v)))
            (testing (str "variable " v " rank is an integer")
              (is (integer? (:rank vm))))
            (testing (str "variable " v " has valid type")
              (is (contains? key/variable-types (:type vm))))
            (testing (str "variable " v " dimensions is a vector of 1 or more")
              (is (vector? (:dimensions vm)))
              (is (>= (count (:dimensions vm)) 0))
              (is (every? (fn [d]
                            (every? key/dimensions (keys d)))
                          (:dimensions vm))))
            (testing (str "variable " v " has a positive size")
              (is (> (:size vm) 0)))
            (testing (str "variable " v " shape is a vector")
              (is (vector? (:shape vm))))
            (testing (str "variable " v " has valid ranges")
              (is (vector? (:ranges vm)))
              (is (every? (fn [r]
                            (every? key/ranges (keys r)))
                          (:ranges vm))))
            (testing (str "variable " v " has valid attributes")
              (is (vector? (:attributes vm)))
              (is (every? (fn [a]
                            (every? key/attributes (keys a)))
                          (:attributes vm))))))))))

(deftest variable-testing
  (doseq [f test-files]
    (reset! test-file (str (:test-data conf) "/" f))
    (variables-test)
    (variable-test)))

(defn test-ns-hook []
  (variable-testing))

