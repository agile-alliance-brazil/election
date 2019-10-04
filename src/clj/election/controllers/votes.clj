(ns election.controllers.votes
  (:require
    [clojure.tools.logging :as log]
    [election.views.votes :as view]
    [election.db.candidates :as db]
    [election.db.tokens :as tokens]
    [election.db.elections :as elections]
    [election.routes.paths :as paths]
    [election.i18n.messages :as i18n]
    [ring.util.response :refer [redirect]]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
    [election.controllers.elections :as election-controller]))

(defn- valid-token? [election-id token]
  (not (nil? (tokens/get-valid-token election-id token))))

(defn- check-election-start [election]
  (if (t/after? (t/now) (c/from-sql-time (:startdate election)))
    :after
    :before))
(defn- check-election-end [election]
  (if (t/before? (t/now) (c/from-sql-time (:enddate election)))
    :before
    :after))
(defn- in-election-phase? [election]
  (and
    (= :after (check-election-start election))
    (= :before (check-election-end election))
  )
)

(defn- build-flash [request election token]
  {
    :type :error
    :message
    (i18n/t request
      (cond
        (= :before (check-election-start election)) :votes/not-started
        (= :after (check-election-end election)) :votes/too-late
        :else :votes/used-token
      )
    )
  }
)

(defn new-vote [{{election-id :election-id token :token} :params :as request}]
  (let [election (elections/election (Integer. election-id))]
    (if (and (in-election-phase? election) (valid-token? (:id election) token))
      (view/place-vote-view
        request
        (assoc election :candidates (db/candidates-for (:id election)))
      )
      (->
        (redirect (paths/election-path (:id election)))
        (assoc :flash (build-flash request election token))
      )
    )
  )
)

(defn- register-vote [request election vote token]
  (if (valid-token? election token)
    (and
      (db/register-vote election vote)
      (tokens/mark-as-used election token)
      {:type :notice :message (i18n/t request :votes/recorded)})
    {:type :error :message (i18n/t request :votes/used-token)}
  )
)

(defn- valid-vote? [election-id votes]
  (let [election (elections/election election-id)
    candidates (db/candidates-for election-id)
    ids (map (fn [candidate] (str (:id candidate))) candidates)
    casted-votes (count (filter (fn [id] (some #(= id %) ids)) votes))]
    (=
      casted-votes
      (:candidatestoelect election)
    )
  )
)

(defn place [{{election :election-id token :token votes :vote} :params :as request}]
  (let [election-id (Integer. election)]
    (if (valid-vote? election-id votes)
      (->
        (redirect (paths/election-path election-id))
        (assoc
          :flash
          (register-vote request election-id (map #(Integer. %) votes) token)
        )
      )
      (new-vote
        (->
          request
          (assoc :flash {:type :error :message (i18n/t request :votes/invalid)})
        )
      )
    )
  )
)
