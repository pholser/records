(ns records.rest
  (:gen-class)
  (:require [records.core :as rec]
            [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [jsonista.core :as json]))

(def records-store (atom []))

(defn records-sorted-by [criterion]
  (->> @records-store
    (sort-by criterion)
    (json/write-value-as-string)))

(defn records-json [recs]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body recs})

(defn add-record [body type]
  (let [newrec (rec/line->record body (rec/suffix->delimiter type))]
    (swap! records-store conj newrec)
    {:status 200
     :content-type "application/jsonplain"
     :body (json/write-value-as-string newrec)}))
  
(defn records-by-color [req]
  (records-json (records-sorted-by :favorite-color)))
(defn records-by-birthdate [req]
  (records-json (records-sorted-by :birthdate)))
(defn records-by-name [req]
  (records-json (records-sorted-by (juxt :last-name :first-name))))

(defroutes app-routes
  (POST "/records" [type :as {body :body}]
    (add-record (slurp body) (keyword type)))
  (GET "/records/color" [] records-by-color)
  (GET "/records/birthdate" [] records-by-birthdate)
  (GET "/records/name" [] records-by-name)
  (route/not-found "error, no route"))

(defn -main
  "Read files named in command line args, sort and print"
  [& args]
  (swap! records-store conj (mapcat rec/file->records args))
  (server/run-server (wrap-defaults #'app-routes api-defaults)
                     {:port 3000})
  (println (str "Running API on port 3000")))
