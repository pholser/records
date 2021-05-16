(ns records.core
  (:gen-class)
  (:require [clojure.string :as str])
  (:import (javax.mail.internet InternetAddress)
           (java.time LocalDate)))

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

(defn normalize-birthdate [birthdate]
  (->> birthdate
    (str/trim)
    (LocalDate/parse)))
    
(def normalizers
  [str/trim
   str/trim
   normalize-email
   normalize-favorite-color
   normalize-birthdate])
   
(defn record-split [raw delimiter]
  (str/split raw delimiter 5))

(defn line->record [line delimiter]
  (->> (record-split line delimiter)
    (map #(%1 %2) normalizers)
    (apply ->Record)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
