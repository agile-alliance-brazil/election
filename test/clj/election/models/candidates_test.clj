(ns election.models.candidates-test
  (:use clojure.test election.test-helper)
  (:require
    [election.models.candidates :as candidates]
  )
)

(deftest picture-url
  (testing "picture-url for nil is nil"
    (is (= nil (candidates/picture-url nil)))
  )
  (testing "picture-url for empty candidate is nil"
    (is (= nil (candidates/picture-url {})))
  )
  (testing "picture-url for candidate without email is nil"
    (is (= nil (candidates/picture-url {:id 1})))
  )
  (testing "picture-url for candidate with email is gravatar's url for email"
    (is (= "https://secure.gravatar.com/avatar/93942e96f5acd83e2e047ad8fe03114d" (candidates/picture-url {:email "test@email.com"})))
  )
)
