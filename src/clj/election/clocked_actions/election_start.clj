(ns election.clocked-actions.election-start
  (:require
    [clojure.tools.logging :as log]
    [election.java-bridge :as bridge]
    [election.views.mailer :as view]
    [election.db.elections :as elections]
    [election.db.tokens :as tokens]
    [election.db.voters :as voters]
    [election.i18n.messages :as i18n]
    [election.services.mailer :as mailer]
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
  (mailer/send-email
    {
      :to email
      :subject (i18n/t {:locale i18n/preferred-language} :mailer/token/subject name)
      :body (view/election-token-email-body token)
    }
  )
)

(defn- register-tokens [election tokens]
  (map
    (fn [token]
      (try
        (and (tokens/save-token token) (send-email token))
        (catch Exception e
          (log/error "Error while processing token with ID " (:id token) " for email " (:email token) ": " (.getMessage e))
          (.printStackTrace e)
          { :code 1 :error :FAILED :message (.getMessage e) :token-id (:id token) :email (:email token) }
        )
      )
    )
    (map #(assoc % :election election) tokens)
  )
)

(defn notify-voters [election]
  (let [voters (voters/voters-for (:id election))]
    (log/info "Notifying voters for election " (:id election) " to a total of " (count voters))
    (doall (register-tokens election (generate-tokens-for voters)))
  )
)

(defn send-voter-tokens [last-run-datetime]
  (let [elections (elections/started-since-now-and last-run-datetime)]
    (reduce merge {}
      (map
        (fn [election]
          (let [results (notify-voters election)
            successes (filter #(= (:code %) 0) results)
            failures (filter #(= (:code %) 1) results)]
            {
              (:id election)
              {
                :success (count successes)
                :failures (map (fn [f] {:email (:email f) :token (:token f)}) failures)
              }
            }
          )
        )
        elections
      )
    )
  )
)
