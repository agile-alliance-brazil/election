(ns election.views.elections
  (:require
    [clojure.tools.logging :as log]
    [hiccup.page :as page]
    [hiccup.element :refer (link-to)]
    [hiccup.form :as form]
    [optimus.link :as link]
    [election.views.layout :as layout]
    [election.views.votes :as votes]
    [election.routes.paths :as paths]
    [election.db.tokens :as tokens]
    [election.db.candidates :as candidates]
    [election.models.candidates :as candidate]
    [election.authorization :as auth]
    [ring.util.anti-forgery :refer [anti-forgery-field]]
    [ring.util.response :refer [response]]
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

(defn- sorted-candidates-by-vote [election-id]
  (sort-by
    (juxt :votecount :fullname)
    (compare-many [> compare])
    (candidates/candidates-for election-id)
  )
)

(defn show-view [{session :session :as request} {election-id :id end-date :enddate start-date :startdate :as election}]
  (log/info "Rendering election show view with " election)
  (layout/election-layout (assoc request :election election)
    [:div
      (if (auth/can-register-voters? election (:user session))
        (link-to (paths/new-election-voters-path election-id) "Register voters")
      )
      (if (t/before? (t/now) (c/from-sql-time end-date))
        [:h3 "Partial results"]
        [:h3 "Final results"]
      )
      [:ul
        (map
          (fn [candidate] (votes/render-candidate-base candidate (fn [c] [:p "Votes: " (:votecount c)])))
          (sorted-candidates-by-vote election-id)
        )
      ]
    ]
  )
)

(defn show-json-view [_ {election-id :id name :name end-date :enddate start-date :startdate :as election}]
  (let [voter-count (tokens/token-count-for election-id)
    vote-count (tokens/used-token-count-for election-id)]
    (response
      {
        :id election-id
        :name name
        :startDate start-date
        :endDate end-date
        :registeredVotersCount voter-count
        :votesCastCount vote-count
        :candidates
        (map
          (fn [c]
            {
              :name (:fullname c)
              :pictureUrl (candidate/picture-url c)
              :voteCount (:votecount c)
            }
          )
          (sorted-candidates-by-vote election-id))
      }
    )
  )
)

(defn new-voters-view [request {election-id :id :as election}]
  (layout/election-layout (assoc request :election election)
    (form/form-to {:enctype "multipart/form-data"} [:put (paths/register-election-voters-path election-id)]
      (anti-forgery-field)
      (form/file-upload :voters)
      (form/submit-button {:disabled false} "Add voter list")
    )
  )
)
