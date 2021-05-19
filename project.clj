(defproject records "0.1.0-SNAPSHOT"
  :description "Clojure REST API exploration"
  :url "http://github.com/pholser/records"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [javax.mail/mail "1.4.3"]
                 [clojure.java-time "0.3.2"]
		 [compojure "1.6.1"]
		 [http-kit "2.3.0"]
		 [ring/ring-defaults "0.3.2"]
		 [metosin/jsonista "0.3.3"]
		 [talltale "0.4.3"]]
  :main nil
  :target-path "target/%s"
  :profiles {:main-core {:main records.core}
             :main-rest {:main records.rest}
	     :uberjar {:aot :all}})
