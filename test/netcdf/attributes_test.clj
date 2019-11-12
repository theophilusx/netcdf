(ns netcdf.attributes-test
  (:require [netcdf.attributes :as sut]
            [clojure.test :refer [deftest testing is]]
            [netcdf.file :refer [with-netcdf]]
            [cprop.core :refer [load-config]]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-file (str (:test-data conf) "/test.nc"))

(deftest global-attributes-test
  (with-netcdf [nc test-file]
    (let [attributes (sut/global-attributes nc)]
      (testing "returns result as vector"
        (is (vector? attributes)))
      (testing "returns vector of maps with correct keys"
        (is (every? (fn [a]
                      (= [:name :type :length :value] (keys a)))
                    attributes))))))

(deftest global-attribute-test
  (with-netcdf [nc test-file]
    (let [ct (sut/global-attribute nc "createTime")]
      (testing "return value is a map"
        (is (map? ct)))
      (testing "has correct keys"
        (is (= [:name :type :length :value] (keys ct))))
      (testing "Has correct values for each key"
        (is (= (:name ct) "createTime"))
        (is (= (:type ct) :int))
        (is (= (:length ct) 1))
        (is (= (:value ct) 1552083984)))
      (testing "non-existent attributes"
        (is (nil? (sut/global-attribute nc "doesnotexist")))))))

 (deftest attribute-test
  (with-netcdf [nc test-file]
    (let [cts (sut/attribute nc "@creationTimeString")]
      (testing "returns a map"
        (is (map? cts)))
      (testing "has correct keys"
        (is (= [:name :type :length :value] (keys cts))))
      (testing "has correct values"
        (is (= (:name cts) "creationTimeString"))
        (is (= (:type cts) :String))
        (is (= (:length cts) 1))
        (is (= (:value cts) "Fri Mar  8 22:26:24 2019")))
      (testing "non-existent attribute returns nil"
        (is (nil? (sut/attribute nc "nosuchattribute")))))))



