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
    [election.db.voters :as voters]
    [election.db.candidates :as candidates]
    [election.models.users :as users]
    [election.models.candidates :as candidate]
    [election.authorization :as auth]
    [election.i18n.messages :as i18n]
    [election.io-config :as io-config]
    [ring.util.anti-forgery :refer [anti-forgery-field]]
    [ring.util.response :refer [response]]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
  )
)

(defn list-view [{{user :user} :session :as request} {elections :elections}]
  (log/info "Rendering election list view for " user)
  (layout/layout request
    [:div
      [:ul.elections
        (list*
          (map
            (fn [{id :id name :name}]
              [:li (link-to (paths/election-path id) name)])
            elections
          )
        )
      ]
      [:ul.actions
        (if (users/admin? user)
          [:li (link-to (paths/path-for paths/new-election-matcher) (i18n/t request :elections/create))]
        )
      ]
    ]
  )
)

(defn- compare-many [comps]
  (fn [xs ys]
    (if-let [result (first (drop-while zero? (map (fn [f x y] (. f (compare x y))) comps xs ys)))]
      result
      0
    )
  )
)

(defn- sorted-candidates-by-vote [election-id]
  (sort-by
    (juxt :votecount :fullname)
    (compare-many [> compare])
    (candidates/candidates-for election-id)
  )
)

(defn show-view [{{user :user} :session :as request} {election-id :id end-date :enddate start-date :startdate :as election}]
  (log/info "Rendering election show view with " election)
  (let [candidates (sorted-candidates-by-vote election-id)
    voter-count (voters/voters-count-for election-id)
    expected-votes (* (:candidatestovoteon election) voter-count)
    votes-received (reduce + (map #(:votecount %) candidates))]
    (layout/election-layout (assoc request :election election)
      [:div
        [:ul.actions
          (if (auth/can-register-voters? election user)
            [:li (link-to (paths/new-election-voters-path election-id) (i18n/t request :votes/register))]
          )
          (if (auth/can-add-candidates? election user)
            [:li (link-to (paths/new-election-candidate-path election-id) (i18n/t request :candidates/add))]
          )
        ]
        (if (and (t/before? (t/now) (c/from-sql-time end-date)) (< votes-received expected-votes))
          [:h3 (i18n/t request :votes/partial-results)]
          [:h3 (i18n/t request :votes/final-results)]
        )
        [:table.vote-count.summary
          [:thead
            [:tr
              (if (not= voter-count expected-votes) [:th (i18n/t request :votes/voter-count)])
              [:th (i18n/t request :votes/votes-count)]
              [:th (i18n/t request :votes/casted-votes-count)]
            ]
          ]
          [:tbody
            [:tr
              (if (not= voter-count expected-votes) [:td voter-count])
              [:td expected-votes]
              [:td votes-received]
            ]
          ]
        ]
        [:ul.candidates
          (map
            (fn [candidate] (votes/render-candidate-base candidate (fn [c]
              (if (auth/can-edit-candidate? election user c)
                (link-to (paths/edit-election-candidate-path election-id (:id c)) (i18n/t request :candidates/edit (:fullname c)))
                [:h4.votecount (:votecount c)
                  [:span (i18n/t request :votes/count) ]
                ]
              )
            )))
            candidates
          )
        ]
      ]
    )
  )
)

(defn show-json-view [_ {election-id :id name :name end-date :enddate start-date :startdate :as election}]
  (let [candidates (sorted-candidates-by-vote election-id)
    voter-count (voters/voters-count-for election-id)
    vote-count (reduce + (map #(:votecount %) candidates))]
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
          candidates)
      }
    )
  )
)

(defn new-voters-view [request {election-id :id :as election}]
  (layout/election-layout (assoc request :election election)
    (form/form-to {:enctype "multipart/form-data"} [:put (paths/register-election-voters-path election-id)]
      (anti-forgery-field)
      (form/file-upload :voters)
      (form/submit-button (i18n/t request :votes/add-voters))
    )
  )
)

(defn- element-with-label [label-text [tag-name {element-name :name} :as element]]
  [:fieldset.label-input-holder
    (form/label element-name label-text)
    element
  ]
)

(defn- input-with-label [label-text {input-name :name :as input-options}]
  (element-with-label label-text [:input input-options])
)

(defn new-candidate-view [request {election-id :id :as election}]
  (layout/election-layout (assoc request :election election)
    (form/form-to [:post (paths/register-election-candidate-path election-id)]
      (anti-forgery-field)
      (input-with-label (i18n/t request :candidates/fullname) {:type "text" :name "candidate[fullname]" :id "candidate[fullname]" :required true :autofocus true})
      (input-with-label (i18n/t request :candidates/email) {:type "email" :name "candidate[email]" :id "candidate[email]" :required true})
      (input-with-label (i18n/t request :candidates/twitter) {:type "text" :name "candidate[twitterhandle]" :id "candidate[twitterhandle]"})
      (element-with-label (i18n/t request :candidates/motivation) (form/text-area {:required true} "candidate[motivation]"))
      (input-with-label (i18n/t request :candidates/region) {:type "text" :name "candidate[region]" :id "candidate[region]"})
      (element-with-label (i18n/t request :candidates/minibio) (form/text-area {} "candidate[minibio]"))
      (form/submit-button (i18n/t request :candidates/add))
    )
  )
)

(defn edit-candidate-view [request {election-id :id :as election} {candidate-id :id :as candidate}]
  (layout/election-layout (assoc request :election election)
    (form/form-to [:put (paths/election-candidate-path election-id candidate-id)]
      (anti-forgery-field)
      (input-with-label (i18n/t request :candidates/fullname) {:type "text" :name "candidate[fullname]" :id "candidate[fullname]" :required true :autofocus true :value (:fullname candidate)})
      (input-with-label (i18n/t request :candidates/email) {:type "email" :name "candidate[email]" :id "candidate[email]" :required true :value (:email candidate)})
      (input-with-label (i18n/t request :candidates/twitter) {:type "text" :name "candidate[twitterhandle]" :id "candidate[twitterhandle]" :value (:twitterhandle candidate)})
      (element-with-label (i18n/t request :candidates/motivation) (form/text-area {:required true} "candidate[motivation]" (:motivation candidate)))
      (input-with-label (i18n/t request :candidates/region) {:type "text" :name "candidate[region]" :id "candidate[region]" :value (:region candidate)})
      (element-with-label (i18n/t request :candidates/minibio) (form/text-area "candidate[minibio]" (:minibio candidate)))
      (form/submit-button (i18n/t request :candidates/update))
    )
  )
)

(defn new-view [request]
  ; TODO: Add modernizr and handle datetime input
  (layout/layout request
    (form/form-to [:post (paths/path-for paths/elections-matcher {})]
      (anti-forgery-field)
      (input-with-label (i18n/t request :elections/name) {:type "text" :name "election[name]" :id "election[name]" :required true :autofocus true})
      (element-with-label (i18n/t request :elections/description) (form/text-area {:required true} "election[description]"))
      (input-with-label (i18n/t request :elections/startdate) {:type "datetime" :name "election[startdate]" :id "election[startdate]" :placeholder io-config/datetime-input-pattern :required true})
      (input-with-label (i18n/t request :elections/enddate) {:type "datetime" :name "election[enddate]" :id "election[enddate]" :placeholder io-config/datetime-input-pattern :required true})
      (input-with-label (i18n/t request :elections/candidates-to-elect) {:type "number" :name "election[candidatestoelect]" :id "election[candidatestoelect]" :min 1 :value 1 :required true})
      (input-with-label (i18n/t request :elections/candidates-to-vote-on) {:type "number" :name "election[candidatestovoteon]" :id "election[candidatestovoteon]" :min 1 :value 1 :required true})
      (form/submit-button (i18n/t request :elections/create))
    )
  )
)
