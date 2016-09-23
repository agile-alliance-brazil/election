(ns election.views.votes
  (:require
    [clojure.tools.logging :as log]
    [election.views.layout :as layout]
    [election.routes.paths :as paths]
    [election.db.elections :as elections]
    [election.models.candidates :as candidates]
    [election.i18n.messages :as i18n]
    [hiccup.form :refer (form-to text-field submit-button)]
    [ring.util.anti-forgery :refer [anti-forgery-field]]
    [hiccup.form :as form]
    [markdown.core :as md]
  )
)

(defn- render-position [pos]
  (let [name (str "vote-" pos)]
    [:input{:type "number" :class "vote" :name "vote[]" :id name}]
  )
)

(defn render-candidate-base [candidate extra-content-function]
  [:li.candidate{:data-candidate-id (:id candidate) :id (str "candidate-" (:id candidate))}
    [:div.top
      [:img.photo{:src (candidates/picture-url candidate)}]
      [:h3.name (:fullname candidate)]
    ]
    [:div.bio
      (extra-content-function candidate)
    ]
  ]
)

(defn- render-candidate [candidate]
  (render-candidate-base candidate (fn [c] [:p (md/md-to-html-string (:minibio c))]))
)

(defn place-vote-view [{{token :token :as params} :params :as request} {election-id :id candidates :candidates :as election}]
  (log/info "Rendering place vote view for election with id " election-id " and request " params)
  (layout/election-layout (assoc request :election election)
    [:div
      [:p.instructions (i18n/t request :votes/instructions (:candidatestoelect election))]
      (form-to {:class "vote"} [:post (paths/place-vote-path election-id token)]
        (anti-forgery-field)
        [:ul.candidates
          (list* (map render-candidate (shuffle candidates)))
          (submit-button {:class "clear" :disabled "disabled" :data-confirm-message (i18n/t request :votes/confirm)} (i18n/t request :votes/place))
        ]
        (list* (map render-position (range 1 (+ 1 (:candidatestovoteon election)))))
      )
    ]
  )
)
