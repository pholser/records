(ns records.core
  (:gen-class)
  (:require [clojure.string :as str])
  (:import (javax.mail.internet InternetAddress)))

(defrecord Record
  [last-name first-name email favorite-color birthdate])

(defn normalize-email [addr]
  (->> addr
    (str/trim)
    (InternetAddress/parse)
    (first)
    (str/lower-case)))

(defn normalize-favorite-color [color]
  (str/lower-case (str/trim color)))

(def normalizers
  [str/trim
   str/trim
   normalize-email
   normalize-favorite-color
   str/trim])
   
(defn record-split [raw delimiter]
  (str/split raw delimiter 5))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
