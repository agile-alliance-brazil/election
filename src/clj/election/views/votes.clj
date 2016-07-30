(ns election.views.votes
  (:require
    [clojure.tools.logging :as log]
    [election.views.layout :as layout]
    [election.routes.paths :as paths]
    [hiccup.form :refer (form-to text-field submit-button)]
  )
)

; TODO: Decompose
(defn place-vote-view [request data]
  (let [params (:params request)]
    (log/info "Rendering place vote view with " data " and request " params)
    (layout/election-layout request
      (form-to {} [:post (paths/place-vote-path (:election-id params) (:token params))]
        ; TODO Add antiforgery token
        [:ul
          (list*
            (map
              (fn [candidate]
                [:li (:name candidate)])
              (:candidates data)
            )
          )
        ]
        (submit-button "Vote")
      )
    )
  )
)