(ns election.clocked-actions.clock
  (:gen-class)
  (:require
    [clojure.tools.logging :as log]
    [environ.core :refer [env]]
    [election.db.clocks :as db]
    [cheshire.core :as cheshire]
    [election.clocked-actions.election-start :as election-start]
    [election.clocked-actions.election-close-to-end :as election-close-to-end]
  )
)

(def clock-interval (Integer. (or (:clock-interval env) "500")))

(def actions
  [
    #'election-start/send-voter-tokens
    #'election-close-to-end/send-voter-reminder
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
          (.printStackTrace e)
          {(.getMessage e) (map str (.getStackTrace e))}
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
  (println "Starting clock process with interval " clock-interval "ms...")
  (while true
    (run (:id (db/create-new-run)) (:enddate (db/last-clock-info)))
    (Thread/sleep clock-interval)
  )
)
