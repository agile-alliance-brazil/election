(ns election.services.mailer
  (:require
    [postal.core :as postal]
    [environ.core :refer [env]]
  )
)

(defn send-email [content]
  (postal/send-message
    {
      :user (:aws-user env)
      :pass (:aws-pass env)
      :host (:aws-host env)
      :port 587
    }
    (merge {:from (:email-sender env)} content)
  )
)
