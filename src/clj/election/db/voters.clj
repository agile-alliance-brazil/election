(ns election.db.voters
  (:require
    [clojure.tools.logging :as log]
    [clojure.java.jdbc :as j]
    [election.db.config :as db-config]
    [honeysql.core :as sql]
    [honeysql.helpers :as h]
  )
)

(defn voters-count-for [election-id]
  (let [
    conditions (->
      (h/select :%count.*)
      (h/from :voters)
      (h/where [:= :electionid election-id]))
      query (sql/format conditions)
    ]
    (->> conditions
      sql/format
      (j/query (db-config/dbspec))
      first
      :count
    )
  )
)

(defn voters-for [election-id]
  (let [
    conditions (->
      (h/select :*)
      (h/from :voters)
      (h/where [:= :electionid election-id]))
      query (sql/format conditions)
    ]
    (log/debug "Querying DB for voters with " query)
    (j/query (db-config/dbspec) query)
  )
)

(defn register-voters [election-id voter-list]
  (if (empty? voter-list)
    0
    (let [emails (map second voter-list)
      preregistered-emails-query (-> (h/select :email) (h/from :voters) (h/where [:and [:= :electionid election-id] [:in :email emails]]) sql/format)
      preregistered-emails-results (j/query (db-config/dbspec) preregistered-emails-query)
      preregistered-emails (map #(:email %) (j/query (db-config/dbspec) preregistered-emails-query) [])
      known-email (fn [email] (some #(= email %) preregistered-emails))
      voters-to-add (filter (fn [voter-info] (not (known-email (second voter-info)))) voter-list)
      adding-count (count voters-to-add)
      insert-query (-> (h/insert-into :voters) (h/values (map (fn [v] {:electionid election-id :fullname (first v) :email (second v)}) voters-to-add)) sql/format)]
      (println "Registering " voters-to-add " after excluding " preregistered-emails)
      (if (< 0 adding-count)
        (do
          (log/debug "Inserting new voters with " insert-query)
          (j/execute! (db-config/dbspec) insert-query)
        )
      )
      adding-count
    )
  )
)
