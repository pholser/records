(ns records.core-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [records.core :refer :all]
	    [talltale.core :as tt]))

(defn generate-record []
  (let [p (tt/person)
        c (tt/color)]
    (->Record (:last-name p)
              (:first-name p)
	      (:email p)
	      c
	      (:date-of-birth p))))

(defn generate-record-file [filename delimiter count]
  (with-open [f (io/writer filename)]
    (doall
      (repeatedly count
        #(.write f
	         (str (record->line (generate-record) delimiter) "\n"))))))

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
  (testing "successful space splitting"
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

(deftest normalizing-color
  (testing "should not regard casing"
    (is (= "blue" (normalize-favorite-color " BlUE   ")))))

(deftest legitimate-iso-birthdate
  (testing "when everything is as expected"
    (is (= (java.time.LocalDate/parse "1970-04-21")
           (normalize-birthdate "1970-04-21")))))

(deftest illegitimate-iso-birthdate
  (testing "when date cannot be parsed"
    (is (thrown?
         java.time.format.DateTimeParseException
	 (normalize-birthdate "1970-04-**")))))

(deftest csv-line->record
  (testing "successful CSV line to record"
    (is (= (->Record
             "Doe"
	     "John"
	     "jdoe@example.com"
	     "blue"
	     (java.time.LocalDate/parse "1970-02-28"))
           (line->record
	     "Doe,John, jdoe@example.com  ,Blue, 1970-02-28 "
	     #",")))))

(deftest pipe-line->record
  (testing "successful pipe line to record"
    (is (= (->Record
             "Doe"
	     "John"
	     "jdoe@example.com"
	     "blue"
	     (java.time.LocalDate/parse "1970-02-28"))
           (line->record
	     "Doe | John| jdoe@example.com  |Blue|1970-02-28  "
	     #"[|]")))))

(deftest spaces-line->record
  (testing "successful space line to record"
    (is (= (->Record
             "Doe"
	     "John"
	     "jdoe@example.com"
	     "blue"
	     (java.time.LocalDate/parse "1970-02-28"))
           (line->record
	     "Doe  John   jdoe@example.com   Blue\t\t1970-02-28  "
	     #"\s+")))))

