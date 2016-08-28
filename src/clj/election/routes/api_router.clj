(ns election.routes.api-router
  (:require
    [compojure.core :refer [defroutes GET PUT POST DELETE ANY context]]
    [ring.util.response :refer [response redirect]]
    [election.routes.paths :as paths]
    [election.controllers.elections :as elections]
    ))

(defroutes routes
  (context "/api" []
    (context "/v1" []
      (context "/elections" []
        (GET (str paths/election-matcher ".json") request (elections/show-json request))
      )
    )
  )
)
