(ns election.db.config
  (:require [environ.core :refer [env]]
    [jdbc.pool.c3p0 :as pool]
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
        }))))

; Using a pool for JDBC connections
(def dbspec (pool/make-datasource-spec jdbc-config))
