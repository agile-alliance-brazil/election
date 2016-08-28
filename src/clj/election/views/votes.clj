(ns election.views.votes
  (:require
    [clojure.tools.logging :as log]
    [election.views.layout :as layout]
    [election.routes.paths :as paths]
    [election.db.elections :as elections]
    [election.models.candidates :as candidates]
    [hiccup.form :refer (form-to text-field submit-button)]
    [ring.util.anti-forgery :refer [anti-forgery-field]]
    [hiccup.form :as form]
  )
)

(defn- render-position [pos]
  (let [name (str "vote-" pos)]
    [:input{:type "number" :class "vote" :name "vote[]" :id name}]
  )
)

(defn render-candidate-base [candidate extra-content-function]
  [:li.candidate{:data-candidate-id (:id candidate) :id (str "candidate-" (:id candidate))}
    [:h3.name (:fullname candidate)]
    [:img.photo{:src (candidates/picture-url candidate)}]
    (extra-content-function candidate)
  ]
)

(defn- render-candidate [candidate]
  (render-candidate-base candidate (fn [c] [:p (:minibio c)]))
)

(defn place-vote-view [{{token :token :as params} :params :as request} {election-id :id candidates :candidates :as election}]
  (log/info "Rendering place vote view with " election " and request " params)
  (layout/election-layout (assoc request :election election)
    (form-to {:class "vote"} [:post (paths/place-vote-path election-id token)]
      (anti-forgery-field)
      [:ul.candidates
        (list* (map render-candidate (shuffle candidates)))
        (submit-button {:class "clear" :disabled "disabled"} "Place vote")
      ]
      (list* (map render-position (elections/positions-to-vote-on election-id)))
    )
  )
)
