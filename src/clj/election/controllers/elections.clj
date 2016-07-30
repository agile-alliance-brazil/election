(ns election.controllers.elections
  (:require
    [election.views.elections :as view]
    [election.db.elections :as db]
  )
)

(defn list-for [request]
  (view/list-view request {:elections (db/elections)})
)

(defn show [request]
  (view/show-view request (db/election (:id request)))
)
