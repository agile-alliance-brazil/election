(ns election.routes.paths
  (:require
    [environ.core :refer [env]]
  )
)

(def elections-matcher "/")
(defn elections-path [] elections-matcher)

(def election-matcher "/:election-id{[0-9]+}")
(defn election-path [election-id] (str "/" election-id))

(def place-vote-matcher "/:election-id{[0-9]+}/:token")
(defn place-vote-path [election-id token] (str "/" election-id "/" token))

(def status-matcher "/status")
(defn status-path [] status-matcher)

(def login-matcher "/login")
(defn login-path [] login-matcher)

(def oauth-callback-matcher "/auth/aab-identity/callback")
(defn oauth-callback-path [] oauth-callback-matcher)

(def logout-matcher "/logout")
(defn logout-path [] logout-matcher)

(def new-election-voters-matcher "/:election-id{[0-9]+}/voters/new")
(defn new-election-voters-path [election-id] (str "/" election-id "/voters/new"))

(def register-election-voters-matcher "/:election-id{[0-9]+}/voters")
(defn register-election-voters-path [election-id] (str "/" election-id "/voters"))

(defn url-for [p]
  (str (:host env) p)
)

(defn home-path []
  (elections-path))
