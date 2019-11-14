(ns netcdf.variables-test
  (:require [netcdf.variables :as sut]
            [clojure.test :refer [deftest testing is]]
            [netcdf.file :refer [with-netcdf]]
            [cprop.core :refer [load-config]]))

(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-file (str (:test-data conf) "/test.nc"))

(deftest variables-test
  (with-netcdf [nc test-file]
    (let [v (sut/variables nc)]
      (testing "variables returns a vector"
        (is (vector? v)))
      (testing "return vector consists of maps with correct keys"
        (is (every? (fn [vm]
                      (= [:description :element-size :name :rank :type
                          :dimensions :size :shape :ranges :obj :attributes]
                         (keys vm)))
                    v))))))

(deftest variable-test-1
  (with-netcdf [nc test-file]
    (let [v (sut/variable nc "/latitude")]
      (testing "variable has correct keys"
        (is (= [:description :element-size :name :rank :type
                :dimensions :size :shape :ranges :obj :attributes] (keys v))))
      (testing "variable has correct values for single dim var"
        (is (= (:description v) nil))
        (is (= (:element-size v) 4))
        (is (= (:name v) "latitude"))
        (is (= (:rank v) 1))
        (is (= (:type v) :float))
        (is (vector? (:dimensions v)))
        (is (= (count (:dimensions v)) 1))
        (is (= [:name :length :unlimited? :varying? :obj]
               (keys (first (:dimensions v)))))
        (is (= (:size v) 735))
        (is (vector? (:shape v)))
        (is (= (count (:shape v)) 1))
        (is (= (first (:shape v)) 735))
        (is (vector? (:ranges v)))
        (is (= (count (:ranges v)) 1))
        (is (map? (first (:ranges v))))
        (is (= (keys (first (:ranges v)))
               [:name :first :last :length :stride :obj]))
        (is (vector? (:attributes v)))
        (is (every? (fn [a]
                      (= (keys a) [:name :type :length :value :obj]))
                    (:attributes v))))
      (testing "variable has correct values for multi dim var"
        (let [x (sut/variable nc "/T_SFC")]
          (is (= (:description x) "Surface Temperature"))
          (is (= (:element-size x) 4))
          (is (= (:name x) "T_SFC"))
          (is (= (:rank x) 3))
          (is (= (:type x) :float))
          (is (vector? (:dimensions x)))
          (is (= (count (:dimensions x)) 3))
          (is (every? (fn [d]
                        (= [:name :length :unlimited? :varying? :obj]
                           (keys d)))
                      (:dimensions x)))
          (is (= (:size x) 85556205))
          (is (vector? (:shape x)))
          (is (= (count (:shape x)) 3))
          (is (= (:shape x) [161 735 723]))
          (is (vector? (:ranges x)))
          (is (= (count (:ranges x)) 3))
          (is (map? (first (:ranges x))))
          (is (= (keys (first (:ranges x)))
                 [:name :first :last :length :stride :obj]))
          (is (vector? (:attributes x)))
          (is (every? (fn [a]
                        (= (keys a) [:name :type :length :value :obj]))
                      (:attributes x))))))))

(deftest variable-test-2
  (with-netcdf [nc test-file]
    (let [v (sut/variable nc nil "latitude")]
      (testing "variable has correct keys"
        (is (= [:description :element-size :name :rank :type
                :dimensions :size :shape :ranges :obj :attributes] (keys v))))
      (testing "variable has correct values for single dim var"
        (is (= (:description v) nil))
        (is (= (:element-size v) 4))
        (is (= (:name v) "latitude"))
        (is (= (:rank v) 1))
        (is (= (:type v) :float))
        (is (vector? (:dimensions v)))
        (is (= (count (:dimensions v)) 1))
        (is (= [:name :length :unlimited? :varying? :obj]
               (keys (first (:dimensions v)))))
        (is (= (:size v) 735))
        (is (vector? (:shape v)))
        (is (= (count (:shape v)) 1))
        (is (= (first (:shape v)) 735))
        (is (vector? (:ranges v)))
        (is (= (count (:ranges v)) 1))
        (is (map? (first (:ranges v))))
        (is (= (keys (first (:ranges v)))
               [:name :first :last :length :stride :obj]))
        (is (vector? (:attributes v)))
        (is (every? (fn [a]
                      (= (keys a) [:name :type :length :value :obj]))
                    (:attributes v))))
      (testing "variable has correct values for multi dim var"
        (let [x (sut/variable nc nil "T_SFC")]
          (is (= (:description x) "Surface Temperature"))
          (is (= (:element-size x) 4))
          (is (= (:name x) "T_SFC"))
          (is (= (:rank x) 3))
          (is (= (:type x) :float))
          (is (vector? (:dimensions x)))
          (is (= (count (:dimensions x)) 3))
          (is (every? (fn [d]
                        (= [:name :length :unlimited? :varying? :obj]
                           (keys d)))
                      (:dimensions x)))
          (is (= (:size x) 85556205))
          (is (vector? (:shape x)))
          (is (= (count (:shape x)) 3))
          (is (= (:shape x) [161 735 723]))
          (is (vector? (:ranges x)))
          (is (= (count (:ranges x)) 3))
          (is (map? (first (:ranges x))))
          (is (= (keys (first (:ranges x)))
                 [:name :first :last :length :stride :obj]))
          (is (vector? (:attributes x)))
          (is (every? (fn [a]
                        (= (keys a) [:name :type :length :value :obj]))
                      (:attributes x))))))))
