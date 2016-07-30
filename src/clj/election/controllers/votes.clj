(ns election.controllers.votes
  (:require
    [election.views.votes :as view]
    [election.db.candidates :as db]
    [election.routes.paths :as paths]
    [ring.util.response :refer [redirect]]
    [election.controllers.elections :as election-controller]))

(defn valid-token? [token]
  true) ; TODO Implement logic

(defn new [request]
  (if (valid-token? (:token request))
    (view/place-vote-view
      request
      {:candidates (db/candidates-for (:election-id request))}
    )
    (election-controller/show request) ; TODO: Flash token already used
  )
)

(defn- register-vote-and-redirect [election-id vote token]
  (redirect (paths/election-path election-id)))

(defn place [request]
  (let [params (:params request)]
    (if (valid-token? (:token params))
      (register-vote-and-redirect (:election-id params) (:vote params) (:token params))
      (election-controller/show request) ; TODO: Flash token already used
    )
  )
)
