(ns election.models.users-test
  (:use clojure.test election.test-helper)
  (:require
    [election.models.users :as users]
  )
)

(deftest admin-test
  (testing "nil user is not admin"
    (is (= false (users/admin? nil)))
  )
  (testing "empty user is not admin"
    (is (= false (users/admin? {})))
  )
  (testing "user with id different than 1 is not admin"
    (is (= false (users/admin? {:id 4})))
  )
  (testing "user with id 1 is admin"
    (is (= true (users/admin? {:id 1})))
  )
)
