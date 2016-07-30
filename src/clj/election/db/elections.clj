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

; TODO: Use real data

; (defn election [id]
;   (let [
;     conditions (->
;       (select :*)
;       (from :elections)
;       (where [:= :id id]))
;     query (sql/format conditions)
;     ]
;     (log/debug "Querying DB for election with " query)
;     (first (j/query db-config/dbspec query))
;   )
; )
(defn election [id]
  {
    :id 1
    :name "Agile Alliance Brazil Board 2017-2019 election"
    ; TODO: Handle dates
    ; :start-date (t/now)
    ; :end-date (t/now)
  }
)

; (defn elections
;   ([]
;     (elections :* {})
;   )
;   ([ids]
;     (elections ids {})
;   )
;   ([columns options]
;     (let [
;       order (if (or (some #{:createdat} (list* columns)) (= :* columns)) {:order-by [[:createdat :asc]]} {})
;       conditions (->
;         (apply select columns)
;         (from :elections)
;         (merge order)
;         (merge options))
;         query (sql/format conditions)
;       ]
;       (log/debug "Querying DB for elections with " query)
;       (j/query db-config/dbspec query)
;     )
;   )
; )
(defn elections []
  [(election 1)]
)