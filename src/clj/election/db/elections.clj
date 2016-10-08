(ns election.db.elections
  (:require
    [clojure.tools.logging :as log]
    [clojure.java.jdbc :as j]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
    [clj-time.format :as f]
    [election.io-config :as io-config]
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
        (h/order-by [:startdate :desc])
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
    (h/where
      [:and
        [:< :startdate (c/to-sql-time (t/now))]
        (if (not (nil? past-time))
          [:>= :startdate (c/to-sql-time past-time)]
          true
        )
      ]
    )
  )
)

(defn- parse-datetime [datetime-str]
  (->> datetime-str
    (f/parse io-config/datetime-input-formatter)
    c/to-sql-time
  )
)

(defn ending-in-days-since [days past-time]
  (elections
    :*
    (h/where
      [:and
        [:< :enddate (c/to-sql-time (t/plus (t/now) (t/days days)))]
        (if (not (nil? past-time))
          [:>= :enddate (c/to-sql-time (t/plus past-time (t/days days)))]
          true
        )
      ]
    )
  )
)

(defn insert-election [{startdate-str :startdate enddate-str :enddate to-elect :candidatestoelect to-vote :candidatestovoteon :as data}]
  (first
    (j/insert!
      (db-config/dbspec)
      :elections
      (merge
        data
        {
          :startdate (parse-datetime startdate-str)
          :enddate (parse-datetime enddate-str)
          :candidatestoelect (Integer. to-elect)
          :candidatestovoteon (Integer. to-vote)
        }
      )
    )
  )
)
