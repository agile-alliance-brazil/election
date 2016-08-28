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
    (fn [voter] {:email (:email voter) :name (:fullname voter) :token (bridge/random-uuid)})
    voters
  )
)

(defn send-email [{{name :name} :election email :email :as token}]
  (postal/send-message
    {
      :user (:aws-user env)
      :pass (:aws-pass env)
      :host (:aws-host env)
      :port 587
    }
    {
      :from (:email-sender env)
      :to email
      :subject (str "You're invited to vote in " name "!")
      :body (mailer/election-token-email-body token)
    }
  )
)

(defn- register-tokens [election tokens]
  (map
    (fn [token] (and (tokens/save-token token) (send-email token)))
    (map #(assoc % :election election) tokens)
  )
)

(defn notify-voters [election-id]
  (let [election (elections/election (read-string election-id))
    voters (voters/voters-for (:id election))]
    (doall (register-tokens election (generate-tokens-for voters)))
  )
)

(defn -main [election-id]
  (notify-voters election-id)
)
