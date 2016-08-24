(ns election.db.status
  (:require
    [clojure.tools.logging :as log]
    [clj-http.client :as client]
    [clojure.java.jdbc :as j]
    [election.db.config :as db-config]
    [honeysql.core :as sql]
  )
)

(defn- hide-password [jdbc-map]
  (merge jdbc-map {:password "hidden"})
)

(defn- last-migration []
  (let [query { :select [:id] :from [:ragtime_migrations] :order-by [[:id :desc]] :limit 1 }
    result (j/query (db-config/dbspec) (sql/format query))]
    (:id (first result))
  )
)

(defn report []
  (try
    (str "DB connection is up. Last migration: " (or (last-migration) "none."))
    (catch Exception e
      (str "DB connection failed. Connection/query to "
        (hide-password db-config/postgres-jdbc-config) " failed with: "
        e (.getMessage e)
      )
    )
  )
)
