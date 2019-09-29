(ns core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [core :as core]))

(def test-data-lines
  ["1999-12-31,1464.469971,1472.420044,1458.189941,1469.250000,1469.250000,374050000"
   "2000-01-03,1469.250000,1478.000000,1438.359985,1455.219971,1455.219971,931800000"
   "2000-01-04,1455.219971,1455.219971,1397.430054,1399.420044,1399.420044,1009000000"
   "2000-01-05,1399.420044,1413.270020,1377.680054,1402.109985,1402.109985,1085500000"
   "2000-01-06,1402.109985,1411.900024,1392.099976,1403.449951,1403.449951,1092300000"])

(def test-data (map core/data-line->coll test-data-lines))
(def test-partitions (core/data-partitions test-data 2))

(deftest data-line->coll-test
  (is (= ["1464.469971" "1472.420044" "1458.189941" "1469.250000" "374050000"]
         (first test-data)))
  (is (= ["1469.250000" "1478.000000" "1438.359985" "1455.219971" "931800000"]
         (second test-data))))

(deftest csv-line-test
  (is (= "a,b,c,d,e\n" (core/csv-line ["a" "b" "c" "d" "e"]))))

(deftest data-partitions-test
  (is (= 3 (count test-partitions)))
  (is (= "1402.109985" (-> test-partitions last last first))))

(deftest xs-test
  (is (= ["1464.469971" "1472.420044" "1458.189941" "1469.250000" "374050000"
          "1469.250000" "1478.000000" "1438.359985" "1455.219971" "931800000"]
         (-> test-partitions first core/xs)))
  (is (= ["1469.250000" "1478.000000" "1438.359985" "1455.219971" "931800000"
          "1455.219971" "1455.219971" "1397.430054" "1399.420044" "1009000000"]
         (-> test-partitions second core/xs))))

(deftest current-closing-price-test
  (is (= 1455.219971 (-> test-partitions first core/current-closing-price)))
  (is (= 1399.420044 (-> test-partitions second core/current-closing-price))))

(deftest next-day-closing-price-test
  (is (= 1399.420044 (-> test-partitions first core/next-closing-price)))
  (is (= 1402.109985 (-> test-partitions second core/next-closing-price))))

(deftest y-test
  (is (= 1 (core/y 1 2)))
  (is (= 0 (core/y 2 1)))
  (is (= 0 (core/y 1 1))))
