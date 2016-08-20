(ns election.views.elections
  (:require
    [clojure.tools.logging :as log]
    [hiccup.page :as page]
    [hiccup.element :refer (link-to)]
    [optimus.link :as link]
    [election.views.layout :as layout]
    [election.views.votes :as votes]
    [election.routes.paths :as paths]
    [election.db.tokens :as tokens]
    [election.db.candidates :as candidates]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
  )
)

(defn list-view [request {elections :elections}]
  (log/info "Rendering election list view")
  (layout/layout request
    [:ul
      (list*
        (map
          (fn [{id :id name :name}]
            [:li (link-to (paths/election-path id) name)])
          elections
        )
      )
    ]
  )
)

(defn- compare-many [comps]
  (fn [xs ys]
    (if-let [result (first (drop-while zero? (map (fn [f x y] (. f (compare x y))) comps xs ys)))]
      result
      0)))

(defn show-view [request {election-id :id name :name end-date :enddate :as data}]
  (log/info "Rendering election show view with " data)
  (layout/layout request
    [:div.election
      [:h1 name]
      (if (t/before? (t/now) (c/from-sql-time end-date))
        [:h3 "Partial results"]
        [:h3 "Final results"]
      )
      [:ul
        (map
          (fn [candidate] (votes/render-candidate-base candidate (fn [c] [:p "Votes: " (:votecount c)])))
          (sort-by (juxt :votecount :fullname) (compare-many [> compare]) (candidates/candidates-for election-id))
        )
      ]
    ]
  )
)
