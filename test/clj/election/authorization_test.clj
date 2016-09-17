(ns election.authorization-test
  (:use clojure.test election.test-helper)
  (:require
    [election.authorization :as authorization]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
  )
)

(deftest can-register-voters-test
  (testing "cannot register voters for election that already started"
    (let [result (authorization/can-register-voters? {:startdate (c/to-sql-time (t/date-time 2016 1 1))} {:id 1})]
      (is (= false result))
    )
  )
  (testing "cannot register voters for election that hasn't started if user is not an admin"
    (let [result (authorization/can-register-voters? {:startdate (c/to-sql-time (t/plus (t/now) (t/weeks 1)))} {:id 10})]
      (is (= false result))
    )
  )
  (testing "cannot register voters for empty election"
    (let [result (authorization/can-register-voters? {} {:id 1})]
      (is (= false result))
    )
  )
  (testing "cannot register voters for election with empty user"
    (let [result (authorization/can-register-voters? {:startdate (c/to-sql-time (t/plus (t/now) (t/weeks 1)))} {})]
      (is (= false result))
    )
  )
  (testing "cannot register voters for nil election"
    (let [result (authorization/can-register-voters? nil {:id 1})]
      (is (= false result))
    )
  )
  (testing "cannot register voters for election with nil user"
    (let [result (authorization/can-register-voters? {:startdate (c/to-sql-time (t/plus (t/now) (t/weeks 1)))} nil)]
      (is (= false result))
    )
  )
  (testing "can register voters for election starting in the future with admin user"
    (let [result (authorization/can-register-voters? {:startdate (c/to-sql-time (t/plus (t/now) (t/weeks 1)))} {:id 1})]
      (is (= true result))
    )
  )
)
