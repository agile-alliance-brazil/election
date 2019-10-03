(ns election.controllers.elections-test
  (:use clojure.test)
  (:require
    [election.controllers.elections :as e]))

(deftest email-validation
  (testing "accepts gmail email"
    (let [result (e/is-valid-email? "someone@gmail.com")]
      (is (= true result))
    )))