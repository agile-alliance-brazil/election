(ns election.connection.status
  (:require
    [clj-http.client :as client]
    [election.i18n.messages :as i18n]
  )
)

(def default-request-url
  "https://www.google.com"
)

(def default-timeout 1000)

(defn- default-request [url timeout]
  {:method :get :url url :socket-timeout timeout :conn-timeout timeout :throw-exceptions false}
)

(defn- timed [op & args]
  (let [writer (java.io.StringWriter.)]
    (merge
      (binding [*out* writer]
        (time (apply op args)))
      {
        :time
        (Integer. (re-find #"\d+" (.toString (.getBuffer writer))))
      }
    )
  )
)

(defn report [request url]
  (try
    (let [response (timed client/request (default-request url default-timeout))]
      (i18n/t request :status/connection/successful url (:status response) (:time response))
    )
    (catch Exception e
      (i18n/t request :status/connection/successful (.getMessage e))
    )
  )
)
