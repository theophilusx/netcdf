(ns theophilusx.netcdf.groups-test
  (:require [theophilusx.netcdf.groups :as sut]
            [clojure.test :refer [deftest testing is]]
            [theophilusx.netcdf.core :refer [with-netcdf]]
            [cprop.core :refer [load-config]]
            [theophilusx.netcdf.keys-test :as key]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-files ["test.nc" "test_hgroups.nc" "testrh.nc"
                 "test_echam_spectral-deflated.nc" "OMI-Aura_L2-example.nc"
                 "access-s.nc"])

(def test-file (atom ""))

(deftest root-group-test
  (testing (str "Testing with file " @test-file)
    (with-netcdf [nc @test-file]
    (let [g (sut/root-group nc)]
      (testing "group is a map with correct keys"
        (is (map? g))
        (is (every? key/groups (keys g))))
      (testing
          "group has expected values"
          (is (= (:short-name g) "Root"))
          (is (= (:root? g) true))
          (is (vector? (:attributes g)))
          (is (every? (fn [a]
                        (every? key/attributes (keys a)))
                      (:attributes g)))
          (is (vector? (:dimensions g)))
          (is (every? (fn [d]
                        (every? key/dimensions (keys d)))
                      (:dimensions g)))
          (is (vector? (:variables g)))
          (is (every? (fn [v]
                        (every? key/variables (keys v)))
                      (:variables g))))))))

(deftest group-testing
  (doseq [f test-files]
    (reset! test-file (str (:test-data conf) "/" f))
    (root-group-test)))

(defn test-ns-hook []
  (group-testing))
