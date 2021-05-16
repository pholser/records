(ns records.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
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

;; Thanks to https://www.rosettacode.org/wiki/Extract_file_extension
(defn file-extension [s]
  (second (re-find #"\.([a-zA-Z0-9]+)$" s)))

(def suffix->delimiter {:txt #"\s+" :csv #"," :pipe #"[|]"})

(defn file->records [filename]
  (let [delim (suffix->delimiter (keyword (file-extension filename)))]
    (with-open [f (io/reader filename)]
      (map #(line->record %1 delim) (doall (line-seq f))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
