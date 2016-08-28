(ns election.authorization
  (:require
    [election.models.users :as users]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
  )
)

(defn can-register-voters? [{start-date :startdate} user]
  (and (t/before? (t/now) (c/from-sql-time start-date)) (users/admin? user))
)
