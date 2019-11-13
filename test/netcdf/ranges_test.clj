(ns netcdf.ranges-test
  (:require  [netcdf.ranges :as sut]
             [clojure.test :refer [deftest testing is]]))

(deftest make-range-test
  (testing "make-range 1"
    (let [r (sut/make-range 10)]
      (is (map? r))
      (is (= [:name :first :last :length :stride :obj] (keys r)))
      (is (= (:name r) nil))
      (is (= (:first r) 0))
      (is (= (:last r) 9))
      (is (= (:length r) 10))
      (is (= (:stride r) 1))))
  (testing "make-range 2"
    (let [r (sut/make-range 0 99)]
      (is (map? r))
      (is (= [:name :first :last :length :stride :obj] (keys r)))
      (is (= (:name r) nil))
      (is (= (:first r) 0))
      (is (= (:last r) 99))
      (is (= (:length r) 100))
      (is (= (:stride r) 1))))
  (testing "make-range 3"
    (let [r (sut/make-range 0 199 1)]
      (is (map? r))
      (is (= [:name :first :last :length :stride :obj] (keys r)))
      (is (= (:name r) nil))
      (is (= (:first r) 0))
      (is (= (:last r) 199))
      (is (= (:length r) 200))
      (is (= (:stride r) 1))))
  (testing "make-range 4"
    (let [r (sut/make-range "latData" 0 199 1)]
      (is (map? r))
      (is (= [:name :first :last :length :stride :obj] (keys r)))
      (is (= (:name r) "latData"))
      (is (= (:first r) 0))
      (is (= (:last r) 199))
      (is (= (:length r) 200))
      (is (= (:stride r) 1)))))




