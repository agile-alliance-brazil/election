(ns election.controllers.votes
  (:require
    [clojure.tools.logging :as log]
    [election.views.votes :as view]
    [election.db.candidates :as db]
    [election.db.tokens :as tokens]
    [election.db.elections :as elections]
    [election.routes.paths :as paths]
    [ring.util.response :refer [redirect]]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
    [election.controllers.elections :as election-controller]))

(defn- valid-token? [election token]
  (not (nil? (tokens/get-valid-token election token))))

(defn- in-election-phase? [election-id]
  (let [election (elections/election election-id)]
    (and
      (t/after? (t/now) (c/from-sql-time (:startdate election)))
      (t/before? (t/now) (c/from-sql-time (:enddate election)))
    )
  )
)

(defn- build-flash [election-id token]
  (let [message (if (in-election-phase? election-id) "Token already used" "Not currently in voting period")]
    {
      :type :error
      :message message
    }
  )
)

(defn new-vote [{{election-id :election-id token :token} :params :as request}]
  (let [election (elections/election (read-string election-id))]
    (if (and (in-election-phase? (:id election)) (valid-token? (:id election) token))
      (view/place-vote-view
        request
        (assoc election :candidates (db/candidates-for (:id election)))
      )
      (->
        (redirect (paths/election-path (:id election)))
        (assoc :flash (build-flash (:id election) token))
      )
    )
  )
)

(defn- register-vote [election vote token]
  (if (valid-token? election token)
    (and
      (db/register-vote election vote)
      (tokens/mark-as-used election token)
      {:type :notice :message "Vote recorded! Thank you."})
    {:type :error :message "Token already used"}
  )
)

(defn- valid-vote? [election votes]
  (let [candidates (db/candidates-for election)
    ids (map (fn [candidate] (str (:id candidate))) candidates)]
    (=
      (count (filter (fn [id] (some #(= id %) ids)) votes))
      (elections/candidates-to-elect-for election)
    )
  )
)

(defn place [{{election :election-id token :token votes :vote} :params :as request}]
  (let [election-id (read-string election)]
    (if (valid-vote? election-id votes)
      (->
        (redirect (paths/election-path election-id))
        (assoc
          :flash
          (register-vote election-id (map read-string votes) token)
        )
      )
      (new-vote
        (->
          request
          (assoc :flash {:type :error :message "Invalid vote. Please ensure you selected the right amount of candidates."})
        )
      )
    )
  )
)
