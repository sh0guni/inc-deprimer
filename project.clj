(defproject inc-deprimer "0.1.0-SNAPSHOT"
  :description "A simple Web API that accepts operations and a list of numbers and returns the result of applying those operations on the numbers"
  :url "https://github.com/sh0guni/inc-deprimer"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.2"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-json "0.3.1"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler inc-deprimer.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
