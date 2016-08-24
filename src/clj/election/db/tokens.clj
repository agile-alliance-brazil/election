(ns election.db.tokens
  (:require
    [clojure.tools.logging :as log]
    [clojure.java.jdbc :as j]
    [clj-time.core :as t]
    [clj-time.local :as l]
    [election.db.config :as db-config]
    [honeysql.core :as sql]
    [honeysql.helpers :as h]
  )
)

(defn get-valid-token [election-id k]
  (let [
    conditions (->
      (h/select :used)
      (h/from :voting_tokens)
      (h/where [:= :electionid election-id])
      (h/merge-where [:= :token k])
      (h/merge-where [:= :used false]))
      query (sql/format conditions)
    ]
    (log/debug "Querying DB for token with " query)
    (first (j/query (db-config/dbspec) query))
  )
)

(defn mark-as-used [election-id k]
  (let [update-statement (->
      (h/update :voting_tokens)
      (h/sset {:used true})
      (h/where [:= :electionid election-id])
      (h/merge-where [:= :token k])
      sql/format)]
    (log/debug "Marking token as used: " update-statement)
    (first (j/execute! (db-config/dbspec) update-statement))
  )
)

(defn save-token [{{election-id :id} :election token :token}]
  (j/insert! (db-config/dbspec) :voting_tokens {:electionid election-id :token token :used false})
)