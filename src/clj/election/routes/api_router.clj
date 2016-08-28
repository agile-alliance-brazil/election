(ns election.routes.api-router
  (:require
    [compojure.core :refer [defroutes GET PUT POST DELETE ANY context]]
    [ring.util.response :refer [response redirect]]
    ))

(defroutes routes
  (context "/api" []
    (context "/v1" []
      (GET "/" request (response {:type "success" :message "Hi world"}))
    )
  )
  
)
