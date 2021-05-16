(ns records.core-test
  (:require [clojure.test :refer :all]
            [records.core :refer :all]))

(deftest split-record-csv
  (testing "successful CSV splitting"
    (is (= ["Doe" "John" "jdoe@example.com" "Blue" "19700228 "]
           (split-record
	     "Doe,John, jdoe@example.com  ,Blue, 19700228 "
	     #"\s*,\s*")))))

(deftest split-record-pipe
  (testing "successful pipe splitting"
    (is (= ["Doe" "John" "jdoe@example.com" "Blue" "19700228  "]
           (split-record
	     "Doe | John| jdoe@example.com  |Blue|19700228  "
	     #"\s*[|]\s*")))))
