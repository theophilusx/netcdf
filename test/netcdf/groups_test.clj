(ns netcdf.groups-test
  (:require [netcdf.groups :as sut]
            [clojure.test :refer [deftest testing is]]
            [netcdf.file :refer [with-netcdf]]
            [cprop.core :refer [load-config]]
            [netcdf.keys :as key]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-file (str (:test-data conf) "/test.nc"))

(deftest root-group-test
  (with-netcdf
    [nc test-file]
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
                        (= [:description :element-size :name :rank :type
                            :dimensions :size :shape :ranges :obj :attributes]
                           (keys v)))
                      (:variables g)))))))
