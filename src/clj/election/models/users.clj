(ns election.models.users
)

; TODO: Actually have a better logic to decide who is admin
(defn admin? [user]
  (or (some #(= (:id user) %) [1]) false)
)
