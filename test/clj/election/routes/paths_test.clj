(ns election.routes.paths-test
  (:use clojure.test election.test-helper)
  (:require
    [election.routes.paths :as paths]
  )
)

(deftest path-for-test
  (testing "path for template without placeholder is template"
    (is (= "/" (paths/path-for "/" {:election-id 1})))
    (is (= "/my-path" (paths/path-for "/my-path" {:election-id 1})))
  )
  (testing "path for template with unmatched placeholder throws exception"
    (is (thrown-with-msg? IllegalArgumentException #"Missing keys: :id" (paths/path-for "/:id" {})))
    (is (thrown-with-msg? IllegalArgumentException #"Missing keys: :election-id, :id" (paths/path-for "/:election-id/:id" {})))
  )
  (testing "path for template with matched placeholder is placeholder replaced by value"
    (is (= "/1" (paths/path-for "/:election-id" {:election-id 1})))
    (is (= "/1/token" (paths/path-for "/:election-id/:token" {:election-id 1 :token "token"})))
  )
  (testing "path for template with matched placeholder with restrictions is placeholder replaced by value"
    (is (= "/1" (paths/path-for "/:election-id{[0-9]+}" {:election-id 1})))
    (is (= "//token" (paths/path-for "/:election-id{[0-9]+}/:token" {:election-id nil :token "token"})))
    (is (= "/1/token" (paths/path-for "/:election-id{[0-9]+}/:token" {:election-id 1 :token "token"})))
  )
  (testing "path for template with matched placeholder and more with restrictions is placeholder replaced by correct values"
    (is (= "/1" (paths/path-for "/:election-id{[0-9]+}" {:election-id 1 :lala "lili"})))
  )
)

(deftest elections-path-test
  (testing (str "elections-path with any parameter is " paths/elections-matcher)
    (is (= paths/elections-matcher (paths/elections-path)))
  )
)

(deftest election-path-test
  (testing "election-path without election-id throws exception"
    (is (thrown? Exception (paths/election-path)))
  )
  (testing (str "election-path with election-id is " paths/election-matcher)
    (is (= "/1" (paths/election-path 1)))
    (is (= "/1" (paths/election-path "1")))
    (is (= "/alpha" (paths/election-path "alpha")))
  )
)

(deftest place-vote-path-test
  (testing "place-vote-path without election-id and token throws exception"
    (is (thrown? Exception (paths/place-vote-path)))
    (is (thrown? Exception (paths/place-vote-path 1)))
  )
  (testing (str "place-vote-path with election-id and token is " paths/place-vote-matcher)
    (is (= "/1/token" (paths/place-vote-path 1 "token")))
    (is (= "/1/token" (paths/place-vote-path "1" "token")))
    (is (= "/alpha/token" (paths/place-vote-path "alpha" "token")))
    (is (= "/1/abc-def-ghi" (paths/place-vote-path 1 "abc-def-ghi")))
  )
)

(deftest status-path-test
  (testing (str "status-path with any parameter is " paths/status-matcher)
    (is (= paths/status-matcher (paths/status-path)))
  )
)

(deftest login-path-test
  (testing (str "login-path with any parameter is " paths/login-matcher)
    (is (= paths/login-matcher (paths/login-path)))
  )
)

(deftest oauth-callback-path-test
  (testing (str "oauth-callback-path with any parameter is " paths/oauth-callback-matcher)
    (is (= paths/oauth-callback-matcher (paths/oauth-callback-path)))
  )
)

(deftest logout-path-test
  (testing (str "logout-path with any parameter is " paths/logout-matcher)
    (is (= paths/logout-matcher (paths/logout-path)))
  )
)

(deftest new-election-voters-path-test
  (testing "new-election-voters-path without election-id throws exception"
    (is (thrown? Exception (paths/new-election-voters-path)))
  )
  (testing (str "new-election-voters-path with election-id " paths/new-election-voters-matcher)
    (is (= "/1/voters/new" (paths/new-election-voters-path 1)))
    (is (= "/1/voters/new" (paths/new-election-voters-path "1")))
    (is (= "/alpha/voters/new" (paths/new-election-voters-path "alpha")))
  )
)

(deftest register-election-voters-path-test
  (testing "register-election-voters-path without election-id throws exception"
    (is (thrown? Exception (paths/register-election-voters-path)))
  )
  (testing (str "register-election-voters-path with election-id " paths/register-election-voters-matcher)
    (is (= "/1/voters" (paths/register-election-voters-path 1)))
    (is (= "/1/voters" (paths/register-election-voters-path "1")))
    (is (= "/alpha/voters" (paths/register-election-voters-path "alpha")))
  )
)
