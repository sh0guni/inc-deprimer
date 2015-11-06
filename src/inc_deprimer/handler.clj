(ns inc-deprimer.handler
  (:require [inc-deprimer.primality :refer :all]
            [clojure.string :as str]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as ring-json]
            [ring.util.response :as ring-response]))

(def valid-operations
  {:inc          (partial map inc)
   :removePrimes (partial remove prime?)})

(defn applier [value-vector function-vector]
  (let [comp-fn (apply comp (reverse function-vector))]
    (comp-fn value-vector)))

(defn parse-string [s parser validator error-msg]
  (if (seq s)
    (let [parsed (-> s (str/split #",") (parser))]
      (if (validator parsed) parsed {:error error-msg}))
    {:error "Missing parameters"}))

(defn parse-nums [numbers-string]
  (let [zero-to-10k? (every-pred integer? #(>= % 0) #(< % 10000))]
    (parse-string
      numbers-string
      #(map read-string %)
      #(every? zero-to-10k? %)
      "Invalid numbers")))

(defn parse-fns [function-map functions-string]
  (parse-string
    functions-string
    #(map function-map (map keyword %))
    #(not-any? nil? %)
    "Invalid operations"))

(defn handler [numbers operations]
  (let [number-vector (parse-nums numbers)
        function-vector (parse-fns valid-operations operations)]
    (cond
      (:error number-vector) number-vector
      (:error function-vector) function-vector
      :else (applier number-vector function-vector))))

(defn wrap-errors [response]
  (if (:error response)
    {:status 400
     :body response}
    (ring-response/response {:result response})))

(defroutes app-routes
           (GET "/api" [numbers ops]
                (wrap-errors (handler numbers ops)))
           (route/not-found "Not Found"))

(defn handle-exceptions [f]
  (fn [request]
    (try (f request)
         (catch Exception e
           {:status 500
            :body "Exception caught"}))))

(def app
  (handle-exceptions
    (-> app-routes
        (ring-json/wrap-json-response)
        (wrap-defaults site-defaults))))
