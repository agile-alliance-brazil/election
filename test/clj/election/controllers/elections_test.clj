(ns election.controllers.elections-test
  (:use clojure.test)
  (:require
    [election.controllers.elections :as e]))

(deftest basic-email-validation
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
  (testing "accepts organization emails"
    (let [result (e/is-valid-email? "me@agilealliancebrazil.org.br")]
      (is result))
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
(deftest advanced-email-validation
  ; From this website, referencing some strange emails:
  ; https://codefool.tumblr.com/post/15288874550/list-of-valid-and-invalid-email-addresses
  ; Some of the listed emails are valid but not supported.
  ; However, they represent very unusual cases, that we won't try to support

  ; Exceptions:
  ; Valid emails not supported
  ;  - "“email”@example.com"
  ;  - "much.“more\ unusual”@example.com"
  ;  - "very.unusual.“@”.unusual.com@example.com"
  ;  - "very.“(),:;<>[]”.VERY.“very@\\ \"very”.unusual@strange.example.com"
  ; Invalid emails not rejected:
  ;  - "email@example.web" -> invalid top-level domain
  ;  - "email@111.222.333.44444" -> invalid ip address
  (let [emails ["email@example.com"
                "firstname.lastname@example.com"
                "email@subdomain.example.com"
                "firstname+lastname@example.com"
                "email@123.123.123.123"
                "email@[123.123.123.123]"
                "1234567890@example.com"
                "email@example-one.com"
                "_______@example.com"
                "email@example.name"
                "email@example.museum"
                "email@example.co.jp"
                "firstname-lastname@example.com"]]
    (doseq [email emails]
      (testing (str "accepts common email: {" email "}")
        (let [result (e/is-valid-email? email)]
          (is result)
        )
      )
    )
  )

  (let [emails ["plainaddress"
                "#@%^%#$@#$@#.com"
                "@example.com"
                "Joe Smith <email@example.com>"
                "email.example.com"
                "email@example@example.com"
                ".email@example.com"
                "email.@example.com"
                "email..email@example.com"
                "あいうえお@example.com"
                "email@example.com (Joe Smith)"
                "email@example"
                "email@-example.com"
                "email@example..com"
                "Abc..123@example.com"]]
    (doseq [email emails]
      (testing (str "accepts invalid email: {" email "}")
        (let [result (e/is-valid-email? email)]
          (is (not result))
        )
      )
    )
  )
  (let [emails ["“(),:;<>[\\]@example.com"
                "just\"not\"right@example.com"
                "this\\ is\"really\"not\\allowed@example.com"]]
    (doseq [email emails]
      (testing (str "accepts uncommon invalid email: {" email "}")
        (let [result (e/is-valid-email? email)]
          (is (not result))
        )
      )
    )
  )
)

(deftest valid-mail-filtering
  (testing "returns the complete list if all is good"
    (let [voters [[1 "a@gmail.com" "a"]
                  [2 "b@outlook.com" "b"]
                  [3 "c@protonmail.com", "c"]]
          valid (e/keep-valid-mails voters)]
      (is (= voters valid))
    )
  )
  (testing "returns the valid voters if any"
    (let [voters [[1 "bad.mail.com"]
                  [2 "b@outlook.com"]
                  [3 "not@yahoo"]]
          invalid (e/keep-valid-mails voters)]
      (is (= invalid [[2 "b@outlook.com"]]))
    )
  )
)
