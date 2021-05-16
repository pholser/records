(ns records.core
  (:gen-class))

(require '[clojure.string :as str])

(defrecord Record
  [last-name first-name email favorite-color birthdate])

(defn record-split [raw delimiter]
  (str/split raw delimiter 5))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
