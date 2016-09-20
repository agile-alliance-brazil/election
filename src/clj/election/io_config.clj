(ns election.io-config
  (:require
    [clj-time.format :as f]
  )
)

(def datetime-input-pattern "yyyy-MM-dd HH:mm:ss Z")

(def datetime-input-formatter (f/formatter datetime-input-pattern))
