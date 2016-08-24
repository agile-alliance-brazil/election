(ns election.controllers.tokens
  (:gen-class)
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [postal.core :as postal]
    [environ.core :refer [env]]
    [election.views.mailer :as mailer]
    [election.db.elections :as elections]
    [election.db.tokens :as tokens]
  )
)


(defn- voters-from [voter-file]
  (rest ; Removes the header line
    (with-open [in-file (io/reader voter-file)]
      (doall
        (csv/read-csv in-file)
      )
    )
  )
)

(defn- random-token []
  (str (java.util.UUID/randomUUID)))

(defn- generate-tokens-for [voters]
  (map
    (fn [voter] {:email (second voter) :name (first voter) :token (random-token)})
    voters
  )
)

(defn send-email [token]
  (postal/send-message
    {
      :user (:aws-user env)
      :pass (:aws-pass env)
      :host (:aws-host env)
      :port 587
    }
    {
      :from (:email-sender env)
      :to (:email token)
      :subject (str "You're invited to vote in " (:name (:election token)) "!")
      :body (mailer/election-token-email-body token)
    }
  ))

(defn- register-tokens [election-id tokens]
  (let [election (elections/election (read-string election-id))]
    (map
      (fn [token] (and (tokens/save-token token) (send-email token)))
      (map #(merge % {:election election}) tokens)
    )
  )
)

(defn import-voters [election-id & voter-files]
  (let [voters (reduce into [] (map voters-from voter-files))
    new-tokens (register-tokens election-id (generate-tokens-for voters))]
    (doall
      (map
        #(println %)
        new-tokens
      )
    )
  )
)

(defn -main [election-id & voter-files]
  (import-voters election-id (list* voter-files))
)
