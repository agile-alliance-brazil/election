(ns election.models.candidates
  (:require
    [digest :as digest]
  )
)

(defn- md5-sum [content]
  (digest/md5 content))

(defn picture-url [candidate]
  (str "https://secure.gravatar.com/avatar/" (md5-sum (:email candidate)))
)
