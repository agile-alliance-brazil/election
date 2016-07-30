(ns election.views.elections
  (:require
    [clojure.tools.logging :as log]
    [hiccup.page :as page]
    [hiccup.element :refer (link-to)]
    [optimus.link :as link]
    [election.views.layout :as layout]
    [election.routes.paths :as paths]
  )
)

(defn list-view [request data]
  (log/info "Rendering election list view")
  (layout/layout request
    [:ul
      (list*
        (map
          (fn [election]
            [:li (link-to (paths/election-path (:id election)) (:name election))])
          (:elections data)
        )
      )
    ]
  )
)

(defn show-view [request data]
  (log/info "Rendering election show view with " data)
  (layout/layout request
    [:div.election
      [:p "ID: " (:id data)]
      [:p "Name: " (:name data)]
      (link-to (paths/place-vote-path (:id data) "fake-token") "Vote")
    ]
  )
)
