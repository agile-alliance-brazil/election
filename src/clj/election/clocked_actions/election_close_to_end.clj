(ns election.clocked-actions.election-close-to-end
  (:require
    [clojure.tools.logging :as log]
    [election.views.mailer :as view]
    [election.db.elections :as elections]
    [election.db.candidates :as candidates]
    [election.db.voters :as voters]
    [election.i18n.messages :as i18n]
    [election.services.mailer :as mailer]
  )
)

; TODO Do the actual work in a worker and only send a message here

(defn send-email [{{name :name} :election email :email :as voter}]
  (mailer/send-email
    {
      :to email
      :subject (i18n/t {:locale i18n/preferred-language} :mailer/reminder/subject name)
      :body (view/election-reminder-email-body voter)
    }
  )
)

(defn- remind-voters [election voters]
  (map
    (fn [voter]
      (try
        (send-email voter)
        (catch Exception e
          (log/error "Error while reminding email " (:email voter) ": " (.getMessage e))
          (.printStackTrace e)
          { :code 1 :error :FAILED :message (.getMessage e) :email (:email voter)}
        )
      )
    )
    (map #(assoc % :election election) voters)
  )
)

(defn notify-voters [election]
  (let [voters (voters/voters-for (:id election))
    candidates (sort #(compare (:fullname %1) (:fullname %2)) (candidates/candidates-for (:id election)))]
    (log/info "Reminding voters for election " (:id election) " to a total of " (count voters))
    (doall (remind-voters (assoc election :candidates candidates) voters))
  )
)

(defn send-voter-reminder [last-run-datetime]
  (let [elections (elections/ending-in-days-since 3 last-run-datetime)]
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
                :failures (if (not (empty? failures)) (clojure.string/join "," (map #(:email %) failures)))
              }
            }
          )
        )
        elections
      )
    )
  )
)
