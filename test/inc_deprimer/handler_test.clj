(ns inc-deprimer.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [inc-deprimer.handler :refer :all]))

(defn check-content-type [response]
  (is (= (get-in response [:headers "Content-Type"])
         "application/json; charset=utf-8")))

(deftest test-app
  (testing "(1, 2, 3), (\"inc\") -> (2, 3, 4)"
    (let [response (app (mock/request :get "/api?numbers=1,2,3&ops=inc"))]
      (is (= (:status response) 200))
      (check-content-type response)
      (is (= (:body response) "{\"result\":[2,3,4]}"))))

  (testing "(1, 2, 3), (\"removePrimes\") -> (1)"
    (let [response (app (mock/request :get "/api?numbers=1,2,3&ops=removePrimes"))]
      (is (= (:status response) 200))
      (check-content-type response)
      (is (= (:body response) "{\"result\":[1]}"))))

  (testing "(1, 2, 3), (\"inc\", \"removePrimes\") -> (4)"
    (let [response (app (mock/request :get "/api?numbers=1,2,3&ops=inc,removePrimes"))]
      (is (= (:status response) 200))
      (check-content-type response)
      (is (= (:body response) "{\"result\":[4]}"))))

  (testing "no parameters"
    (let [response (app (mock/request :get "/api"))]
      (is (= (:status response) 400))
      (check-content-type response)
      (is (= (:body response) "{\"error\":\"Missing parameters\"}"))))

  (testing "missing operations"
    (let [response (app (mock/request :get "/api?numbers=1,2,3"))]
      (is (= (:status response) 400))
      (check-content-type response)
      (is (= (:body response) "{\"error\":\"Missing parameters\"}"))))

  (testing "missing numbers"
    (let [response (app (mock/request :get "/api?ops=inc,removePrimes"))]
      (is (= (:status response) 400))
      (check-content-type response)
      (is (= (:body response) "{\"error\":\"Missing parameters\"}"))))

  (testing "invalid numbers"
    (let [response (app (mock/request :get "/api?numbers=1,invalid,3&ops=inc,removePrimes"))]
      (is (= (:status response) 400))
      (check-content-type response)
      (is (= (:body response) "{\"error\":\"Invalid numbers\"}"))))

  (testing "accept only integers"
    (let [response (app (mock/request :get "/api?numbers=1,2.0,3&ops=inc"))]
      (is (= (:status response) 400))
      (check-content-type response)
      (is (= (:body response) "{\"error\":\"Invalid numbers\"}"))))

  (testing "accept only natural numbers"
    (let [response (app (mock/request :get "/api?numbers=1,2,-3&ops=inc"))]
      (is (= (:status response) 400))
      (check-content-type response)
      (is (= (:body response) "{\"error\":\"Invalid numbers\"}"))))

  (testing "numbers must be less than or equal to 10000"
    (let [response (app (mock/request :get "/api?numbers=10001,2,3&ops=inc"))]
      (is (= (:status response) 400))
      (check-content-type response)
      (is (= (:body response) "{\"error\":\"Invalid numbers\"}"))))

  (testing "invalid operations"
    (let [response (app (mock/request :get "/api?numbers=1,2,3&ops=inc,invalid"))]
      (is (= (:status response) 400))
      (check-content-type response)
      (is (= (:body response) "{\"error\":\"Invalid operations\"}"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
