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

(deftest post-a-text-record
  (testing "POST /records"
    (let [p-resp
            @(http/post "http://localhost:3000/records"
              {:headers {"Content-type" "text/plain"}
               :query-params {"type" "txt"}
               :body "9000 HAL hal@hal.com pink 1992-01-12"})
	  cooked-post (json/read-value (:body p-resp))]
        (is (= (:status p-resp) 200))
        (is (= "HAL" (get cooked-post "first-name"))))))
