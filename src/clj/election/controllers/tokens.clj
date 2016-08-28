(ns election.controllers.tokens
  (:gen-class)
  (:require
    [postal.core :as postal]
    [environ.core :refer [env]]
    [election.views.mailer :as mailer]
    [election.db.elections :as elections]
    [election.db.voters :as voters]
    [election.db.tokens :as tokens]
    [election.java-bridge :as bridge]
  )
)

(defn- generate-tokens-for [voters]
  (map
    (fn [voter] {:email (second voter) :name (first voter) :token (bridge/random-uuid)})
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
  )
)

(defn- register-tokens [election tokens]
  (map
    (fn [token] (and (tokens/save-token token) (send-email token)))
    (map #(merge % {:election election}) tokens)
  )
)

(defn notify-voters [election-id]
  (let [election (elections/election (read-string election-id))
    voters (voters/voters-for election-id)]
    (register-tokens election (generate-tokens-for voters))
  )
)

(defn -main [election-id]
  (notify-voters election-id)
)
