(ns election.controllers.elections
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [election.views.elections :as view]
    [election.db.elections :as db]
    [election.db.voters :as voters]
    [election.authorization :as auth]
    [election.routes.paths :as paths]
    [election.controllers.tokens :as tokens]
    [ring.util.response :as response]
  )
)

(defn list-for [request]
  (view/list-view request {:elections (db/elections)})
)

(defn show [{{election-id :election-id} :params :as request}]
  (let [election (db/election (read-string election-id))]
    (if (nil? (:id election))
      (response/not-found (slurp (io/resource "404.html")))
      (view/show-view request (db/election (read-string election-id)))
    )
  )
)

(defn show-json  [{{election-id :election-id} :params :as request}]
  (let [election (db/election (read-string election-id))]
    (if (nil? (:id election))
      (response/not-found {:message (str "No election with ID " election-id " found.")})
      (view/show-json-view request election)
    )
  )
)

(defn new-voters [{{election-id :election-id} :params session :session :as request}]
  (let [election (db/election (read-string election-id))]
    (if (auth/can-register-voters? election (:user session))
      (view/new-voters-view request election)
      (->
        (response/redirect (paths/election-path election-id))
        (assoc :flash {:type :error :message "Unauthorized access"})
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

(defn register-voters [{{election-id :election-id {voters-file :tempfile} :voters} :params session :session :as request}]
  (let [election (db/election (read-string election-id))
    response (response/redirect (paths/election-path election-id))]
    (if (auth/can-register-voters? election (:user session))
      (let [added-voters-count (voters/register-voters (:id election) (voters-from voters-file))]
        (if added-voters-count
          (assoc response :flash {:type :notice :message (str added-voters-count " new voters registered.")})
          (assoc response :flash {:type :error :message "Voter registration failed"})
        )
      )
      (assoc response :flash {:type :error :message "Unauthorized access"})
    )
  )
)
