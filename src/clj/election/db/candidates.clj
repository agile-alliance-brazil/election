(ns election.db.candidates
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

(defn candidates-for [election-id]
  (let [
    conditions (->
      (select :*)
      (from :candidates)
      (where [:= :electionid election-id]))
      query (sql/format conditions)
    ]
    (log/debug "Querying DB for elections with " query)
    (j/query db-config/dbspec query)
  )
)

(defn register-vote [election-id candidate-ids]
  (let [command 
    [
      "UPDATE candidates SET votecount = votecount + 1 WHERE electionid = ? AND id IN (?, ?, ?)"
      election-id
      (first candidate-ids) ; TODO: Make it variable for any number of votes
      (second candidate-ids)
      (last candidate-ids)
    ]]
    (j/execute! db-config/dbspec command)
  )
)
