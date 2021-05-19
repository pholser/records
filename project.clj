(defproject records "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [javax.mail/mail "1.4.3"]
                 [clojure.java-time "0.3.2"]
		 [compojure "1.6.1"]
		 [http-kit "2.3.0"]
		 [ring/ring-defaults "0.3.2"]
		 [org.clojure/data.json "0.2.6"]
		 [talltale "0.4.3"]]
  :main nil
  :target-path "target/%s"
  :profiles {:main-core {:main records.core}
             :main-rest {:main records.rest}
	     :uberjar {:aot :all}})
