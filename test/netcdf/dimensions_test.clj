(ns netcdf.dimensions-test
  (:require [netcdf.dimensions :as sut]
            [clojure.test :refer [deftest testing is]]
            [netcdf.file :refer [with-netcdf]]
            [cprop.core :refer [load-config]]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-file (str (:test-data conf) "/test.nc"))

(deftest dimensions-test
  (with-netcdf [nc test-file]
    (let [dim (sut/dimensions nc)]
      (testing "returns a vector"
        (is (vector? dim)))
      (testing "returns correct dimension count"
        (is (= (count dim) 3)))
      (testing "returned vector of maps with correct keys"
        (is (every? (fn [d]
                      (= [:name :length :unlimited? :varying? :obj] (keys d)))
                    dim))))))

(deftest dimension-test
  (testing "latitude dimension has correct keys and values"
    (with-netcdf [nc test-file]
      (let [dim (sut/dimension nc "latitude")]
        (is (= [:name :length :unlimited? :varying? :obj] (keys dim)))
        (is (= (:name dim) "latitude"))
        (is (= (:length dim) 735))
        (is (= (:unlimited? dim) false))
        (is (= (:varying? dim) false)))))
  (testing "time dimension has correct keys and values"
    (with-netcdf [nc test-file]
      (let [dim (sut/dimension nc "time")]
        (is (= [:name :length :unlimited? :varying? :obj] (keys dim)))
        (is (= (:name dim) "time"))
        (is (= (:length dim) 161))
        (is (= (:unlimited? dim) false))
        (is (= (:varying? dim) false)))))
  (testing "non-existent dimension returns nil"
    (with-netcdf [nc test-file]
      (is (nil? (sut/dimension nc "notexist"))))))

