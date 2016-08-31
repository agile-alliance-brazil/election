(ns election.clocked-actions.election-start
  (:require
    [election.java-bridge :as bridge]
    [postal.core :as postal]
    [environ.core :refer [env]]
    [election.views.mailer :as mailer]
    [election.db.elections :as elections]
    [election.db.tokens :as tokens]
    [election.db.voters :as voters]
  )
)

; TODO Do the actual work in a worker and only send a message here

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

(defn notify-voters [election]
  (let [voters (voters/voters-for (:id election))]
    (println "Notifying voters for election " (:id election) " to a total of " (count voters))
    (doall (register-tokens election (generate-tokens-for voters)))
  )
)

(defn send-voter-tokens [last-run-datetime]
  (let [elections (elections/started-since-now-and last-run-datetime)]
    (reduce merge {}
      (map
        (fn [election]
          {(:id election) {:token-count (count (notify-voters election))}}
        )
        elections
      )
    )
  )
)
