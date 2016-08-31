(ns election.clocked-actions.clock
  (:gen-class)
  (:require
    [clojure.tools.logging :as log]
    [environ.core :refer [env]]
    [election.db.clocks :as db]
    [cheshire.core :as cheshire]
    [election.clocked-actions.election-start :as election-start]
  )
)

(def clock-interval (or (read-string (:clock-interval env)) 500))

(def actions
  [
    #'election-start/send-voter-tokens
  ]
)

(defn- safe-run [last-run action]
  (let [action-name (-> action meta :name)]
    {
      action-name
      (try
        (action last-run)
        (catch Exception e
          (log/error "Error running " action-name ": " (.getMessage e))
          {(.getMessage e) (.getStackTrace e)}
        )
      )
    }
  )
)

(defn- create-report [last-run]
  (cheshire/generate-string
    (reduce merge {} (map (fn [a] (safe-run last-run a)) actions))
  )
)

(defn- run [run-id last-run]
  (db/end-run run-id (create-report last-run))
)

(defn -main
  [& m]
  (while true
    (run (:id (db/create-new-run)) (:enddate (db/last-clock-info)))
    (Thread/sleep clock-interval)
  )
)
