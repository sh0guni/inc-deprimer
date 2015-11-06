(ns inc-deprimer.primality)

; Primality test from http://en.wikipedia.org/wiki/Primality_test#Clojure_implementation
(defn prime? [n]
  (let [div? (fn [div] (zero? (rem n div)))]
    (cond
      (<= n 3) (>= n 2)
      (or (div? 2) (div? 3)) false
      :else (loop [i 5]
              (cond
                (> (* i i) n) true
                (or (div? i) (div? (+ i 2))) false
                :else (recur (+ i 6)))))))
