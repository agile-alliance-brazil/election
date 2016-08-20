(ns election.routes.site-router
  (:require
    [clojure.tools.logging :as log]
    [compojure.core :refer [defroutes GET PUT POST DELETE ANY context]]
    [election.controllers.elections :as elections]
    [election.controllers.votes :as votes]
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
)
