(ns election.controllers.elections-test
  (:use clojure.test)
  (:require
    [election.controllers.elections :as e]))

(deftest email-validation
  (testing "accepts gmail email"
    (let [result (e/is-valid-email? "someone@gmail.com")]
      (is result)
    )
  )
  (testing "accepts email with dots"
    (let [result (e/is-valid-email? "a.b.c@yahoo.com")]
      (is result)
    )
  )
  (testing "accepts special gmail with +"
    (let [result (e/is-valid-email? "some+one@gmail.com")]
      (is result)
    )
  )
  (testing "rejects email without @"
    (let [result (e/is-valid-email? "agmail.com")]
      (is (not result))
    )
  )
  (testing "rejects email without domain"
    (let [result (e/is-valid-email? "some@gmail")]
      (is (not result))
    )
  )
)

(deftest invalid-mail-filtering
  (testing "returns empty list if all is good"
    (let [voters [[1 "a@gmail.com" "a"]
                  [2 "b@outlook.com" "b"]
                  [3 "c@protonmail.com", "c"]]
          invalid (e/get-invalid-mails voters)]
      (is (empty? invalid))
    )
  )
  (testing "returns the wrong voters if any"
    (let [voters [[1 "bad.mail.com"]
                  [2 "b@outlook.com"]
                  [3 "not@yahoo"]]
          invalid (e/get-invalid-mails voters)]
      (is (= invalid [[1 "bad.mail.com"]
                      [3 "not@yahoo"]]))
    )
  )
)
