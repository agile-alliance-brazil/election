(ns election.routes.paths
  (:require
    [environ.core :refer [env]]
  )
)

(def elections-matcher "/")
(defn elections-path [] elections-matcher)

(def election-matcher "/:id{[0-9]+}")
(defn election-path [id] (str "/" id))

(def place-vote-matcher "/:election-id{[0-9]+}/:token")
(defn place-vote-path [election-id token] (str "/" election-id "/" token))

(def status-matcher "/status")
(defn status-path [] status-matcher)

(defn url-for [p]
  (str (:host env) p)
)
