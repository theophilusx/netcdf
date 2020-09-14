(ns theophilusx.netcdf.data-test
  (:require [theophilusx.netcdf.arrays :as arr]
            [theophilusx.netcdf.data :as sut]
            [theophilusx.netcdf.core :refer [with-netcdf]]
            [theophilusx.netcdf.variables :refer [variable variables]]
            [cprop.core :refer [load-config]]
            [clojure.test :refer [deftest testing is]]))


(def conf (load-config :file "/home/tim/Projects/clojure/netcdf/config.edn"))

(def test-files ["test.nc" "test_hgroups.nc" "testrh.nc"
                 "test_echam_spectral-deflated.nc" "OMI-Aura_L2-example.nc"
                 "access-s.nc"])

(def test-file (atom ""))

(deftest read-scalar
  (testing (str "Test with file " @test-file)
    (with-netcdf [nc @test-file]
      (let [vars (variables nc)]
        (doseq [v vars]
          (when (:is-scalar? v)
            (let [val (sut/read-scalar v)]
              (testing (str "Read scalar " (:type v) " from " (:name v))
                (is (not (nil? val)))
                (is (number? val))))))))))

(deftest read-values
  (testing "Reading single value"
    (reset! test-file (str (:test-data conf) "/access-s.nc"))
    (with-netcdf [nc @test-file]
      (let [v (variable nc "tasmax")]
        (testing (str "reading " (:name v))
          (testing "0 300 400"
            (let [val (sut/read-value v [0 300 400])]
              (is (= (float 23.84) val))))
          (testing "0 300 401"
            (let [val (sut/read-value v [0 300 401])]
              (is (= (float 23.9) val))))
          (testing "0 301 400"
            (let [val (sut/read-value v [0 301 400])]
              (is (= (float 24) val))))
          (testing "0 301 401"
            (let [val (sut/read-value v [0 301 401])]
              (is (= (float 24) val))))
          (testing "1 300 400"
            (let [val (sut/read-value v [1 300 400])]
              (is (= (float 24.94) val))))
          (testing "1 300 401"
            (let [val (sut/read-value v [1 300 401])]
              (is (= (float 24.96) val))))
          (testing "1 301 400"
            (let [val (sut/read-value v [1 301 400])]
              (is (= (float 25.04) val))))
          (testing "1 301 401"
            (let [val (sut/read-value v [1 301 401])]
              (is (= (float 25.01) val)))))))))

(deftest read-slice
  (testing "Reading slice of data"
    (reset! test-file (str (:test-data conf) "/access-s.nc"))
    (with-netcdf [nc @test-file]
      (testing "Reading tasmax variable"
        (let [v (variable nc "tasmax")
              d (sut/read-slice v [0 300 400] [2 2 2])]
          (testing "Array data type"
            (is (= :float (:type d))))
          (testing "Array element type"
            (is (= :float (:elementType d))))
          (testing "Array rank"
            (is (= 3 (:rank d))))
          (testing "Array shape"
            (is (= [2 2 2] (:shape d))))
          (testing "Array size"
            (is (= 8 (:size d))))
          (testing "get 0 0 0"
            (is (= (float 23.84) (arr/get-value d 0 0 0))))
          (testing "get 0 0 1"
            (is (= (float 23.9) (arr/get-value d 0 0 1))))
          (testing "get 0 1 0"
            (is (= (float 24) (arr/get-value d 0 1 0))))
          (testing "get 0 1 1"
            (is (= (float 24) (arr/get-value d 0 1 1))))
          (testing "get 1 0 0"
            (is (= (float 24.94) (arr/get-value d 1 0 0))))
          (testing "get 1 0 1"
            (is (= (float 24.96) (arr/get-value d 1 0 1))))
          (testing "get 1 1 0"
            (is (= (float 25.04) (arr/get-value d 1 1 0))))
          (testing "get 1 1 1"
            (is (= (float 25.01) (arr/get-value d 1 1 1)))))))))

(deftest read-linear
  (testing "Reading linear values from array"
    (reset! test-file (str (:test-data conf) "/access-s.nc"))
    (with-netcdf [nc @test-file]
      (let [v (variable nc "tasmax")
            d (sut/read-slice v [0 300 400] [2 2 2])]
        (testing "linear read 0"
          (is (= (float 23.84) (arr/get-linear-value d 0))))
        (testing "linear read 1"
          (is (= (float 23.9) (arr/get-linear-value d 1))))
        (testing "linear read 2"
          (is (= (float 24) (arr/get-linear-value d 2))))
        (testing "linear read 3"
          (is (= (float 24) (arr/get-linear-value d 3))))
        (testing "linear read 4"
          (is (= (float 24.94) (arr/get-linear-value d 4))))
        (testing "linear read 5"
          (is (= (float 24.96) (arr/get-linear-value d 5))))
        (testing "linear read 6"
          (is (= (float 25.04) (arr/get-linear-value d 6))))
        (testing "linear read 7"
          (is (= (float 25.01) (arr/get-linear-value d 7))))))))
