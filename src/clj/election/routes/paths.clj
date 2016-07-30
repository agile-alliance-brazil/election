(ns election.routes.paths
)

(def elections-matcher "/")
(defn elections-path [] elections-matcher)

(def election-matcher "/:id{[0-9]+}")
(defn election-path [id] (str "/" id))

(def place-vote-matcher "/:election-id{[0-9]+}/:token")
(defn place-vote-path [election-id token] (str "/" election-id "/" token))

(def status-matcher "/status")
(defn status-path [] status-matcher)
