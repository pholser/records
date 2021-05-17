(ns records.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
	    [java-time :as time])
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

(defn record->line [record delimiter]
  (->> (map #(%1 record)
            [:last-name :first-name :email :favorite-color :birthdate])
       (str/join delimiter)))

(defn record->display [record delimiter]
  (->> (map #(%1 record)
            [:last-name
	     :first-name
	     :email
	     :favorite-color
	     #(time/format "M/d/yyyy" (:birthdate %1))])
       (str/join delimiter)))

;; Thanks to https://www.rosettacode.org/wiki/Extract_file_extension
(defn file-extension [s]
  (second (re-find #"\.([a-zA-Z0-9]+)$" s)))

(def suffix->delimiter {:txt #"\s+" :csv #"," :pipe #"[|]"})

(defn file->records [filename]
  (let [delim (suffix->delimiter (keyword (file-extension filename)))]
    (with-open [f (io/reader filename)]
      (map #(line->record %1 delim) (doall (line-seq f))))))

(defn -main
  "Read files named in command line args, sort and print"
  [& args]
  (let [recs
          (mapcat file->records args)
        color-then-last-ascending
	  (sort-by (juxt :favorite-color :last-name) recs)
	birthdate-ascending
	  (sort-by :birthdate recs)
	last-descending
	  (reverse (sort-by :last-name recs))]
    (println "Output 1 - by favorite color then last name ascending:")
    (println "=====")
    (doseq [r color-then-last-ascending] (println (record->display r ",")))
    (println)
    (println "Output 2 - by birth date ascending:")
    (println "=====")
    (doseq [r birthdate-ascending] (println (record->display r ",")))
    (println)
    (println "Output 3 - by last name descending:")
    (println "=====")
    (doseq [r last-descending] (println (record->display r ",")))))