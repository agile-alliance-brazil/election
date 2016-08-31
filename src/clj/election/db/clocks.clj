(ns election.db.clocks
  (:require
    [clojure.tools.logging :as log]
    [clojure.java.jdbc :as j]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
    [election.db.config :as db-config]
    [honeysql.core :as sql]
    [honeysql.helpers :as h]
  )
)

(defn last-clock-info []
  (let [
    conditions (->
      (h/select :*)
      (h/from :clock_runs)
      (h/where [:not= :enddate nil])
      (h/order-by [:startdate :desc])
      (h/limit 1))
    query (sql/format conditions)
    ]
    (log/debug "Querying DB for last clock run with " query)
    (first (j/query (db-config/dbspec) query))
  )
)

(defn create-new-run []
  (first (j/insert! (db-config/dbspec) :clock_runs {:startdate (c/to-sql-time (t/now))}))
)

(defn end-run [run-id report]
  (let [update-statement (->
      (h/update :clock_runs)
      (h/sset {:enddate (c/to-sql-time (t/now)) :report report})
      (h/where [:= :id run-id])
      sql/format)]
    (log/debug "Ending run " run-id " with report " report)
    (first (j/execute! (db-config/dbspec) update-statement))
  )
)
