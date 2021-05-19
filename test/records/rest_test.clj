(ns records.rest-test
  (:require [clojure.test :refer :all]
            [org.httpkit.client :as http]
	    [jsonista.core :as json]))

(deftest get-records-ordered-by-color
  (testing "GET /records/color"
    (let [{:keys [status headers body error] :as resp}
          @(http/get "http://localhost:3000/records/color")
	  cooked (first (json/read-value body))]
      (is (= status 200))
      (is (= 300 (count cooked)))
      (is (= (sort-by :color cooked) cooked)))))

(deftest get-records-ordered-by-name
  (testing "GET /records/name"
    (let [{:keys [status headers body error] :as resp}
          @(http/get "http://localhost:3000/records/name")
	  cooked (first (json/read-value body))]
      (is (= status 200))
      (is (= 300 (count cooked)))
      (is (= (sort-by (juxt :last-name :first-name) cooked) cooked)))))

(deftest get-records-ordered-by-birthdate
  (testing "GET /records/birthdate"
    (let [{:keys [status headers body error] :as resp}
          @(http/get "http://localhost:3000/records/birthdate")
	  cooked (first (json/read-value body))]
      (is (= status 200))
      (is (= 300 (count cooked)))
      (is (= (sort-by :birthdate cooked) cooked)))))
