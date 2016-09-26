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

(defn register-voters [{{election-id :election-id {voters-file :tempfile} :voters} :params {user :user} :session :as request}]
  (let [election (db/election (Integer. election-id))
    response (response/redirect (paths/election-path election-id))]
    (if (auth/can-register-voters? election user)
      (let [voters (voters-from voters-file)
        added-voters-count (voters/register-voters (:id election) voters)]
        (if added-voters-count
          (assoc response :flash {:type :notice :message (i18n/t request :elections/new-voters-registered added-voters-count)})
          (assoc response :flash {:type :error :message (i18n/t request :elections/voter-registration-failed)})
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

(defn- cleanup-parameters [valid-params data]
  (into {}
    (filter
      (fn [[k v]] contains? valid-params k)
      data
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
