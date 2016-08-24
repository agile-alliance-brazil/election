(ns election.sample-test
  (:use clojure.test election.test-helper)
  (:require [election.web :as web])
)

(deftest dev-mode-test
  (testing "should be true"
    (let [result (web/in-dev?)]
      (is (= true result))
    )
  )
)
