(ns election.db.status
  (:require
    [clojure.tools.logging :as log]
    [clj-http.client :as client]
    [clojure.java.jdbc :as j]
    [election.db.config :as db-config]
    [election.i18n.messages :as i18n]
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

(defn report [request]
  (try
    (i18n/t request :status/db-connection/successful (or (last-migration) (i18n/t request :none)))
    (catch Exception e
      (i18n/t request :status/db-connection/failed (hide-password db-config/postgres-jdbc-config) e (.getMessage e))
    )
  )
)
