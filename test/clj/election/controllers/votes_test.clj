(ns election.controllers.votes-test
  (:use clojure.test)
  (:require
    [election.controllers.votes :as v]
  )
)

(deftest check-error-flash-message
	(let [f #'v/build-flash]
		(testing "??"
			(is (= false true))
		)
	)
)