(ns election.models.candidates
  (:require
    [digest :as digest]
  )
)

(defn picture-url [{email :email}]
  (if (nil? email)
    nil
    (str "https://secure.gravatar.com/avatar/" (digest/md5 email))
  )
)
