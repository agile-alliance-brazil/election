(ns election.services.scheduled
  (:require
    [clojurewerkz.quartzite.scheduler :as qs]
    [clojurewerkz.quartzite.jobs :as j]
    [clojurewerkz.quartzite.triggers :as t]
    [clojurewerkz.quartzite.schedule.simple :as s]
  )
)

(j/defjob NoOpJob
  [ctx]
  (comment "Does nothing")
)

(defn -main
  [& m]
  (let [s (-> (qs/initialize) qs/start)
    job
    (j/build
      (j/of-type NoOpJob)
      (j/with-identity (j/key "jobs.noop.1")))
    trigger
    (t/build
      (t/with-identity (t/key "triggers.1"))
      (t/start-now)
      (t/with-schedule (s/schedule
        (s/with-repeat-count 10)
        (s/with-interval-in-milliseconds 200))))]
  )
)
