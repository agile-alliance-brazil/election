(ns election.db.candidates
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

(defn candidates-for [election-id]
  [{:name "Ceci"} {:name "Erica"} {:name "Rodrigo"} {:name "Celso"} {:name "Annelise"} {:name "Mari"}]
)
