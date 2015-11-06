(ns inc-deprimer.applier-test
  (:require [clojure.test :refer :all]
            [inc-deprimer.handler :refer :all]))

(def map-inc (get valid-operations :inc))
(def remove-primes (get valid-operations :removePrimes))
(def ten-integers [1514 5730 7237 113 3683 9004 7112 2573 3902 1777])

(deftest test-applier
  (testing "inc operation"
    (is (= [2 3 4] (applier [1 2 3] [map-inc]))))

  (testing "removePrimes operation"
    (is (= [1] (applier [1 2 3] [remove-primes]))))

  (testing "empty number-vector"
    (is (= [] (applier [] [map-inc]))))

  (testing "empty operations-vector"
    (is (= [1 2 3] (applier [1 2 3] []))))

  (testing "empty parameters"
    (is (= [] (applier [] []))))

  (testing "several operations"
    (is (= [1519 5735 7242 118 3688 7117 2578 1782]
           (applier ten-integers
                    [map-inc map-inc map-inc remove-primes
                     map-inc map-inc remove-primes])))))
