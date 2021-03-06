(ns election.controllers.elections
  (:require
    [clojure.tools.logging :as log]
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [election.views.elections :as view]
    [election.db.elections :as db]
    [election.db.voters :as voters]
    [election.authorization :as auth]
    [election.routes.paths :as paths]
    [election.i18n.messages :as i18n]
    [ring.util.response :as response]
    [election.db.candidates :as candidates]
  )
)

(defn list-for [{session :session :as request}]
  (log/info "Rendering new election for " session)
  (view/list-view request {:elections (db/elections)})
)

(defn show [{{election-id :election-id} :params :as request}]
  (let [election (db/election (Integer. election-id))]
    (if (nil? (:id election))
      (response/not-found (slurp (io/resource "404.html")))
      (view/show-view request election)
    )
  )
)

(defn show-json  [{{election-id :election-id} :params :as request}]
  (let [election (db/election (Integer. election-id))]
    (if (nil? (:id election))
      (response/not-found {:message (i18n/t request :elections/not-found election-id)})
      (view/show-json-view request election)
    )
  )
)

(defn new-voters [{{election-id :election-id} :params {user :user} :session :as request}]
  (let [election (db/election (Integer. election-id))]
    (if (auth/can-register-voters? election user)
      (view/new-voters-view request election)
      (->
        (response/redirect (paths/election-path election-id))
        (assoc :flash {:type :error :message (i18n/t request :forbidden)})
      )
    )
  )
)

(defn- voters-from [voter-file]
  (rest ; Removes the header line
    (with-open [in-file (io/reader voter-file)]
      (doall
        (csv/read-csv in-file)
      )
    )
  )
)

; RegExp from http://emailregex.com/
(def email-pattern #"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])")
(defn is-valid-email? [value]
  (re-matches email-pattern value))
(defn keep-valid-mails [voters]
  (filter
    (comp is-valid-email? second)
    voters))

(defn register-voters [{{election-id :election-id {voters-file :tempfile} :voters} :params {user :user} :session :as request}]
  (let [election (db/election (Integer. election-id))
    response (response/redirect (paths/election-path election-id))]
    (if (auth/can-register-voters? election user)
      (let [voters (voters-from voters-file)
            valid-voters (keep-valid-mails voters)
            added-voters-count (voters/register-voters (:id election) valid-voters)]
        (cond
          ; Failure during the process
          (not added-voters-count)
          (assoc response :flash {:type :error :message (i18n/t request :elections/voter-registration-failed)})
          ; all mails where added
          (= added-voters-count (count voters))
          (assoc response :flash {:type :notice :message (i18n/t request :elections/new-voters-registered added-voters-count)})
          ; mails were added, but some were already present and others were invalid
          :else
          (assoc
            response
            :flash {
              :type :notice
              :message (i18n/t
                request
                :elections/some-voters-registered
                added-voters-count
                (- (count valid-voters) added-voters-count)
                (- (count voters) (count valid-voters))
              )
            }
          )
        )
      )
      (assoc response :flash {:type :error :message (i18n/t request :forbidden)})
    )
  )
)

(defn- cleanup-parameters [valid-params data]
  (into {}
    (filter
      (fn [[k v]] contains? valid-params k)
      data
    )
  )
)

(defn new-candidate [{{election-id :election-id} :params {user :user} :session :as request}]
  (let [election (db/election (Integer. election-id))]
    (if (auth/can-add-candidates? election user)
      (view/new-candidate-view request election)
      (->
        (response/redirect (paths/election-path election-id))
        (assoc :flash {:type :error :message (i18n/t request :forbidden)})
      )
    )
  )
)

(def valid-candidate-params [:fullname :fullname :motivation :email :minibio :region :twitterhandle])

(defn register-candidate [{{election-id :election-id candidate-data :candidate} :params {user :user} :session :as request}]
  (let [election (db/election (Integer. election-id))
    response (response/redirect (paths/election-path election-id))]
    (if (auth/can-add-candidates? election user)
      (let [safe-data (cleanup-parameters valid-candidate-params candidate-data)]
        (if (is-valid-email? (:email safe-data))
          (let [new-candidate (candidates/register-candidate (:id election) safe-data)]
            (if (nil? (:id new-candidate))
              (-> (view/new-candidate-view request election)
                ; TODO: Detail potential errors
                (assoc :flash {:type :error :message (i18n/t request :candidates/create-failed)})
              )
              (-> response
                (assoc :flash {:type :notice :message (i18n/t request :candidates/added)})
              )
            )
          )
          (assoc
            response
            :flash {
              :type :error
              :message (i18n/t request :candidates/invalid-email)
            }
          )
        )
      )
      (assoc response :flash {:type :error :message (i18n/t request :forbidden)})
    )
  )
)

(defn edit-candidate [{{election-id :election-id candidate-id :candidate-id} :params {user :user} :session :as request}]
  (let [election (db/election (Integer. election-id))
      candidate (candidates/candidate-for (Integer. candidate-id))
    ]
    (if (auth/can-edit-candidate? election user candidate)
      (view/edit-candidate-view request election candidate)
      (->
        (response/redirect (paths/election-path election-id))
        (assoc :flash {:type :error :message (i18n/t request :forbidden)})
      )
    )
  )
)

(defn update-candidate [{{election-id :election-id candidate-id :candidate-id candidate-data :candidate} :params {user :user} :session :as request}]
  (let [election (db/election (Integer. election-id))
    candidate (candidates/candidate-for (Integer. candidate-id))
    response (response/redirect (paths/election-path election-id))]
    (if (auth/can-edit-candidate? election user candidate)
      (let [
          safe-data (cleanup-parameters valid-candidate-params candidate-data)
          updated-candidate (candidates/update-candidate (:id candidate) safe-data)
        ]
        (-> response
          (assoc :flash {:type :notice :message (i18n/t request :candidates/updated)})
        )
      )
      (assoc response :flash {:type :error :message (i18n/t request :forbidden)})
    )
  )
)

(defn new-election [{{user :user :as session} :session :as request}]
  (log/info "Rendering new election for " session)
  (if (auth/can-create-election? user)
    (view/new-view request)
    (->
      (response/redirect (paths/home-path))
      (assoc :flash {:type :error :message (i18n/t request :forbidden)})
    )
  )
)

(def valid-new-election-params [:name :description :startdate :enddate :candidatestoelect :candidatestovoteon])

(defn create-election [{{election-data :election} :params {user :user} :session :as request}]
  (if (auth/can-create-election? user)
    (let [new-election (db/insert-election (cleanup-parameters valid-new-election-params election-data))]
      (if (nil? (:id new-election))
        (-> (view/new-view request)
          ; TODO: Detail potential errors
          (assoc :flash {:type :error :message (i18n/t request :elections/create-election-failed)})
        )
        (-> (response/redirect (paths/path-for paths/election-matcher {:election-id (:id new-election)}))
          (assoc :flash {:type :notice :message (i18n/t request :elections/election-created)})
        )
      )
    )
    (-> (response/redirect (paths/home-path))
      (assoc :flash {:type :error :message (i18n/t request :forbidden)})
    )
  )
)
