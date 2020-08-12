(ns theophilusx.netcdf.attributes-test
  (:require [theophilusx.netcdf.attributes :as sut]
            [clojure.test :refer [deftest testing is]]
            [theophilusx.netcdf.core :refer [with-netcdf]]
            [cprop.core :refer [load-config]]
            [theophilusx.netcdf.keys-test :as key]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-files ["test.nc" "test_hgroups.nc" "testrh.nc"
                 "test_echam_spectral-deflated.nc" "OMI-Aura_L2-example.nc"
                 "access-s.nc"])

(def test-file (atom ""))

(deftest global-attributes-test
  (testing (str "Test with file " @test-file)
    (with-netcdf [nc @test-file]
      (let [attributes (sut/global-attributes nc)]
        (testing "returns result as vector"
          (is (vector? attributes)))
        (testing "returns vector of maps with correct keys"
          (is (every? (fn [a]
                        (every? key/attributes (keys a)))
                      attributes)))))))

(deftest global-attribute-value-test
  (testing (str "Test with file " @test-file)
    (with-netcdf [nc @test-file]
      (let [ga (sut/global-attributes nc)
            a-names (map (fn [a]
                            (:name a))
                          ga)]
        (doseq [a a-names]
          (let [attr (sut/global-attribute nc a)]
            (testing (str "attribute " a " value is a maps")
              (is (map? attr)))
            (testing (str "attribute " a " has correct keys")
              (is (every? key/attributes (keys attr))))
            (testing (str "attribute " a " has acceptable values")
              (is (= (:name attr) a))
              (is (contains? key/attribute-types (:type attr)))
              (is (number? (:length attr)))
              (is (not (nil? (:value attr)))))))))))

(deftest attribute-test
  (testing (str "Testing with file " @test-file)
    (with-netcdf [nc @test-file]
      (let [ga (sut/global-attributes nc)
            a-names (map #(:name %) ga)]
        (doseq [a a-names]
          (let [attr (sut/attribute nc (str "@" a))]
            (testing (str "attribute " a " is a map")
              (is (map? attr)))
            (testing (str "attribute " a " has correct keys")
              (is (every? key/attributes (keys attr))))
            (testing (str "attribute " a " has acceptable values")
              (is (= (:name attr) a))
              (is (contains? key/attribute-types (:type attr)))
              (is (number? (:length attr)))
              (is (not (nil? (:value attr)))))))))))

(deftest attributes-testing
  (doseq [f test-files]
    (reset! test-file (str (:test-data conf) "/" f))
    (global-attributes-test)
    (global-attribute-value-test)
    (attribute-test)))

(defn test-ns-hook []
  (attributes-testing))





