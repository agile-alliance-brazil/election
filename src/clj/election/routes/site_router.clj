(ns election.routes.site-router
  (:require
    [clojure.tools.logging :as log]
    [compojure.core :refer [defroutes GET PUT POST DELETE ANY context]]
    [election.controllers.elections :as elections]
    [election.controllers.votes :as votes]
    [election.controllers.session :as session]
    [election.views.status :as status]
    [election.routes.paths :as paths]
  )
)

(defroutes routes
  (GET paths/elections-matcher request (elections/list-for request))
  (GET paths/election-matcher request (elections/show request))
  (GET paths/place-vote-matcher request (votes/new-vote request))
  (POST paths/place-vote-matcher request (votes/place request))
  (GET paths/status-matcher request (status/render-view request))
  (GET paths/login-matcher request (session/new-session request))
  (GET paths/oauth-callback-matcher request (session/login request))
  (GET paths/logout-matcher request (session/destroy-session request))
  (GET paths/new-election-voters-matcher request (elections/new-voters request))
  (PUT paths/register-election-voters-matcher request (elections/register-voters request))
  (GET paths/new-election-matcher request (elections/new-election request))
  (POST paths/elections-matcher request (elections/create-election request))
)
