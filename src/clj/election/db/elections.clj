(ns election.db.elections
  (:require
    [clojure.tools.logging :as log]
    [clojure.java.jdbc :as j]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
    [clj-time.local :as l]
    [election.db.config :as db-config]
    [honeysql.core :as sql]
    [honeysql.helpers :as h]
  )
)

(defn election [id]
  (let [
    conditions (->
      (h/select :*)
      (h/from :elections)
      (h/where [:= :id id]))
    query (sql/format conditions)
    ]
    (log/debug "Querying DB for election with " query)
    (first (j/query (db-config/dbspec) query))
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
        (h/select columns)
        (h/from :elections)
        (merge options))
        query (sql/format conditions)
      ]
      (log/debug "Querying DB for elections with " query)
      (j/query (db-config/dbspec) query)
    )
  )
)

(defn started-since-now-and [past-time]
  (elections
    :*
    (->
      (h/where
        [:and
          [:< :startdate (c/to-sql-time (t/now))]
          [:>= :startdate (c/to-sql-time past-time)]
        ]
      )
    )
  )
)

(defn candidates-to-elect-for [election]
  3
)

(defn positions-to-vote-on [election]
  (range 1 (+ 1 (candidates-to-elect-for election)))
)
