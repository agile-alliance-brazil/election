(ns election.controllers.votes-test
  (:use clojure.test)
  (:require
    [election.controllers.votes :as v]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
    [election.i18n.messages :as i18n]
  )
)

(def past-date (-> (t/now) (t/minus (t/months 1)) (c/to-sql-time)))
(def future-date (-> (t/now) (t/plus (t/months 1)) (c/to-sql-time)))

(deftest check-election-start-detection
  (let [f #'v/check-election-start]
    (testing "detects dates past the election start"
      (let [election {:startdate past-date}]
        (= (f election) :after)
      )
    )
    (testing "detects dates before the election start"
      (let [election {:startdate future-date}]
        (= (f election) :before)
      )
    )
  )
)

(deftest check-election-end-detection
  (let [f #'v/check-election-end]
    (testing "detects dates past the election end"
      (let [election {:enddate past-date}]
        (= (f election) :after)
      )
    )
    (testing "detects dates before the election end"
      (let [election {:enddate future-date}]
        (= (f election) :before)
      )
    )
  )
)

(deftest check-error-flash-message
  (let [f #'v/build-flash-error
        request {:locale :en-US}]
    (testing "generates a message for early votes"
      (let [so-future-date (-> (t/now) (t/plus (t/years 1)) (c/to-sql-time))
            election {:startdate future-date :enddate so-future-date}
            flash (f request election nil)]
        (is (= (:type flash) :error))
        (is (= (:message flash) (i18n/t request :votes/not-started)))
      )
    )
    (testing "generates a message for late votes"
      (let [very-old-date (-> (t/now) (t/minus (t/years 1)) (c/to-sql-time))
            election {:startdate very-old-date :enddate past-date}
            flash (f request election nil)]
        (is (= (:type flash) :error))
        (is (= (:message flash) (i18n/t request :votes/too-late)))
      )
    )
    (testing "generates a message for invalid token"
      (let [election {:startdate past-date :enddate future-date}
            flash (f request election nil)]
        (is (= (:type flash) :error))
        (is (= (:message flash) (i18n/t request :votes/used-token)))
      )
    )
  )
)

