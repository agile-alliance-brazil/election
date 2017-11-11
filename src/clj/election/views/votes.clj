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

(defn render-candidate-base [{:keys [fullname id] :as candidate} extra-content-function]
  [:li.candidate{:data-candidate-id id :id (str "candidate-" id)}
    [:div.top
      [:img.photo{:src (candidates/picture-url candidate)}]
      [:h3.name fullname]
    ]
    [:div.bio
      (extra-content-function candidate)
    ]
  ]
)

(defn- render-candidate [{motivation :motivation :as candidate}]
  (render-candidate-base candidate (fn [c] (md/md-to-html-string motivation)))
)

(defn place-vote-view [{{token :token :as params} :params :as request}
  {:keys [id candidates candidatestoelect candidatestovoteon] :as election}]
  (log/info "Rendering place vote view for election with id " id " and request " params)
  (layout/election-layout (assoc request :election election)
    [:div
      [:p.instructions (i18n/t request :votes/instructions candidatestoelect)]
      (form-to {:class "vote" :data-vote-count candidatestovoteon} [:post (paths/place-vote-path id token)]
        (anti-forgery-field)
        [:ul.candidates
          (list* (map render-candidate (shuffle candidates)))
          (submit-button {:class "clear" :disabled "disabled" :data-confirm-message (i18n/t request :votes/confirm)} (i18n/t request :votes/place))
        ]
        (list* (map render-position (range 1 (+ 1 candidatestovoteon))))
      )
    ]
  )
)
