(ns election.db.config
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [environ.core :refer [env]]
    [clojure.tools.logging :as log]))

(def postgres-url
  (env :postgres-sql))

; If you use a relational DB, using JDBC
(def jdbc-config
  (if (nil? postgres-url)
    {}
    (let [uri-matches (re-matches #"^([^:]+):\/\/(?:([^:]*):([^@]*)@)?([^\s\n]*)" postgres-url)
      subprotocol (nth uri-matches 1)
      subname (str "//" (nth uri-matches 4))
      user (nth uri-matches 2)
      password (nth uri-matches 3)]
      (if (nil? user)
        {
          :subprotocol subprotocol
          :subname subname
        }
        {
          :subprotocol subprotocol
          :subname subname
          :user user
          :password password
        }
      )
    )
  )
)

(defn pool [spec]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname spec)) 
               (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
               (.setUser (:user spec))
               (.setPassword (:password spec))
               ;; expire excess connections after 30 minutes of inactivity:
               (.setMaxIdleTimeExcessConnections (* 30 60))
               ;; expire connections after 3 hours of inactivity:
               (.setMaxIdleTime (* 3 60 60)))] 
    {:datasource cpds})
)

(def pooled-db (delay (pool jdbc-config)))

(defn dbspec [] @pooled-db)
