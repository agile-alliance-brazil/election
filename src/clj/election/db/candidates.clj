(ns election.db.candidates
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

(defn candidates-for [election-id]
  (let [
    conditions (->
      (h/select :*)
      (h/from :candidates)
      (h/where [:= :electionid election-id]))
      query (sql/format conditions)
    ]
    (log/debug "Querying DB for candidates with " query)
    (j/query (db-config/dbspec) query)
  )
)

(defn register-vote [election-id candidate-ids]
  (let [command 
    [ ; TODO: Make it variable for any number of votes
      "UPDATE candidates SET votecount = votecount + 1 WHERE electionid = ? AND id IN (?, ?, ?)"
      election-id
      (first candidate-ids)
      (second candidate-ids)
      (last candidate-ids)
    ]]
    (j/execute! (db-config/dbspec) command)
  )
)

(defn candidate-for [candidate-id]
  (let [
    conditions (->
      (h/select :*)
      (h/from :candidates)
      (h/where [:= :id candidate-id]))
      query (sql/format conditions)
    ]
    (log/debug "Querying DB for candidate with " query)
    (first (j/query (db-config/dbspec) query))
  )
)

(defn register-candidate [election-id candidate]
  (first
    (j/insert!
      (db-config/dbspec)
      :candidates
      (merge
        candidate
        {
          :electionid election-id
        }
      )
    )
  )
)

(defn update-candidate [candidate-id candidate]
  (first
    (j/update!
      (db-config/dbspec)
      :candidates
      candidate
      ["id = ?", candidate-id]
    )
  )
)
