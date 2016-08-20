(ns election.db.elections
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

(defn election [id]
  (let [
    conditions (->
      (select :*)
      (from :elections)
      (where [:= :id id]))
    query (sql/format conditions)
    ]
    (log/debug "Querying DB for election with " query)
    (first (j/query db-config/dbspec query))
  )
)

(defn elections
  ([]
    (elections :* {})
  )
  ([ids]
    (elections ids {})
  )
  ([columns options]
    (let [
      conditions (->
        (select columns)
        (from :elections)
        (merge options))
        query (sql/format conditions)
      ]
      (log/debug "Querying DB for elections with " query)
      (j/query db-config/dbspec query)
    )
  )
)

(defn candidates-to-elect-for [election]
  3
)

(defn positions-to-vote-on [election]
  (range 1 (+ 1 (candidates-to-elect-for election)))
)
