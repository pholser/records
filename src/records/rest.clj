(ns records.rest
  (:gen-class)
  (:require [records.core :as rec]
            [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]))

(defroutes app-routes
  (POST "/records" [] add-record)
  (GET "/records/color" [] records-by-color)
  (GET "/records/birthdate" [] records-by-birthdate)
  (GET "/records/color" [] records-by-name)
  (route/not-found "error, no route"))

(def records-store (atom (file->records "random.csv")))

(def records-by-color [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (str (json/write-str @records-store))})

(defn -main
  "Read files named in command line args, sort and print"
  [& args]
  (println "boo...")
  (server/run-server (wrap-defaults #'app-routes api-defaults)
                     {:port 3000})
  (println (str "Running API on port 3000")))
