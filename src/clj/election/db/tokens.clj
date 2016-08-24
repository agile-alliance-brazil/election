(ns election.db.tokens
  (:require
    [clojure.tools.logging :as log]
    [clojure.java.jdbc :as j]
    [clj-time.core :as t]
    [clj-time.local :as l]
    [election.db.config :as db-config]
    [honeysql.core :as sql]
    [honeysql.helpers :refer :all]
  )
)

(defn get-valid-token [election-id k]
  (let [
    conditions (->
      (select :used)
      (from :voting_tokens)
      (where [:= :electionid election-id])
      (merge-where [:= :token k])
      (merge-where [:= :used false]))
      query (sql/format conditions)
    ]
    (log/debug "Querying DB for token with " query)
    (first (j/query db-config/dbspec query))
  )
)

(defn mark-as-used [election-id k]
  (let [update-statement (->
      (update :voting_tokens)
      (sset {:used true})
      (where [:= :electionid election-id])
      (merge-where [:= :token k])
      sql/format)]
    (log/debug "Marking token as used: " update-statement)
    (first (j/execute! db-config/dbspec update-statement))
  )
)

(defn save-token [{{election-id :id} :election token :token}]
  (j/insert! db-config/dbspec :voting_tokens {:electionid election-id :token token :used false})
)