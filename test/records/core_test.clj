(ns records.core-test
  (:require [clojure.test :refer :all]
            [records.core :refer :all]))

(deftest split-record-csv
  (testing "successful CSV splitting"
    (is (= ["Doe" "John" " jdoe@example.com  " "Blue" " 1970-02-28 "]
           (record-split
	     "Doe,John, jdoe@example.com  ,Blue, 1970-02-28 "
	     #",")))))

(deftest split-record-pipe
  (testing "successful pipe splitting"
    (is (= ["Doe " " John" " jdoe@example.com  " "Blue" "1970-02-28  "]
           (record-split
	     "Doe | John| jdoe@example.com  |Blue|1970-02-28  "
	     #"[|]")))))

(deftest split-record-spaces
  (testing "successful spacee splitting"
    (is (= ["Doe" "John" "jdoe@example.com" "Blue" "1970-02-28  "]
           (record-split
	     "Doe  John   jdoe@example.com   Blue\t\t1970-02-28  "
	     #"\s+")))))

(deftest normalizing-legit-email-addr
  (testing "successful email normalization"
    (is (= "john.doe@example.com"
           (normalize-email "  John.Doe@example.COM ")))))

(deftest list-of-legit-email-addr
  (testing "takes only the first addr"
    (is (= "john.doe@example.com"
           (normalize-email "John.doe@example.com, jdoe@gmail.com")))))

(deftest illegitimate-email-addr
  (testing "should reject non-parseable email addr"
    (is (thrown?
         javax.mail.internet.AddressException
	 (normalize-email "asdfsadfasdfsadf@!#!SADDASDAS")))))

