(ns election.routes.paths
  (:require
    [environ.core :refer [env]]
  )
)

(defn path-for
  ([template]
    (path-for template {})
  )
  ([template {:as params}]
    (let [p
      (reduce
        (fn [route pair]
          (let [pattern (str (first pair) "(?:\\{[^\\}]+\\})?")]
            (clojure.string/replace route (re-pattern pattern) (str (last pair)))
          )
        )
        template
        params
      )
      missing-keys (re-seq #":[^/:{]+" p)
      ]
      (if (empty? missing-keys)
        p
        (throw
          (IllegalArgumentException. (str "Missing keys: " (clojure.string/join ", " missing-keys)))
        )
      )
    )
  )
)

(def elections-matcher "/")
(defn elections-path [] (path-for elections-matcher {}))

(def election-matcher "/:election-id{[0-9]+}")
(defn election-path [election-id] (path-for election-matcher {:election-id election-id}))

(def place-vote-matcher "/:election-id{[0-9]+}/:token")
(defn place-vote-path [election-id token] (path-for place-vote-matcher {:election-id election-id :token token}))

(def status-matcher "/status")
(defn status-path [] (path-for status-matcher {}))

(def login-matcher "/login")
(defn login-path [] (path-for login-matcher {}))

(def oauth-callback-matcher "/auth/aab-identity/callback")
(defn oauth-callback-path [] (path-for oauth-callback-matcher {}))

(def logout-matcher "/logout")
(defn logout-path [] (path-for logout-matcher {}))

(def new-election-voters-matcher "/:election-id{[0-9]+}/voters/new")
(defn new-election-voters-path [election-id] (path-for new-election-voters-matcher {:election-id election-id}))

(def register-election-voters-matcher "/:election-id{[0-9]+}/voters")
(defn register-election-voters-path [election-id] (path-for register-election-voters-matcher {:election-id election-id}))

(def new-election-candidate-matcher "/:election-id{[0-9]+}/candidates/new")
(defn new-election-candidate-path [election-id] (path-for new-election-candidate-matcher {:election-id election-id}))

(def register-election-candidate-matcher "/:election-id{[0-9]+}/candidates")
(defn register-election-candidate-path [election-id] (path-for register-election-candidate-matcher {:election-id election-id}))

(def new-election-matcher "/new")

(defn url-for [p]
  (str (:host env) p)
)

(defn home-path []
  (elections-path))
