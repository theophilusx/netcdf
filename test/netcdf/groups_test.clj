(ns netcdf.groups-test
  (:require [netcdf.groups :as sut]
            [clojure.test :refer [deftest testing is]]
            [netcdf.file :refer [with-netcdf]]
            [cprop.core :refer [load-config]]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-file (str (:test-data conf) "/test.nc"))

(deftest root-group-test
  (with-netcdf [nc test-file]
    (let [g (sut/root-group nc)]
      (testing "group is a map with correct keys"
        (is (map? g))
        (is (= [:short-name :root? :attributes
                :dimensions :variables :obj :children]
               (keys g))))
      (testing "group has expected values"
        (is (= (:short-name g) "Root"))
        (is (= (:root? g) true))
        (is (vector? (:attributes g)))
        (is (every? (fn [a]
                      (= [:name :type :length :value :obj] (keys a)))
                    (:attributes g)))
        (is (vector? (:dimensions g)))
        (is (every? (fn [d]
                      (= [:name :length :unlimited? :varying? :obj]
                         (keys d)))
                    (:dimensions g)))
        (is (vector? (:variables g)))
        (is (every? (fn [v]
                      (= [:description :element-size :name :rank :type
                          :dimensions :size :shape :ranges :obj :attributes]
                         (keys v)))
                    (:variables g)))))))

