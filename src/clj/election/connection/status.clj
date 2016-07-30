(ns election.connection.status
  (:require
    [clj-http.client :as client]))

(def default-request-url
  "https://www.google.com")

(def default-timeout 1000)

(defn- default-request [url timeout]
  {:method :get :url url :socket-timeout timeout :conn-timeout timeout :throw-exceptions false})

(defn- timed [op & args]
  (let [writer (java.io.StringWriter.)]
    (merge
      (binding [*out* writer]
        (time (apply op args)))
      {
        :time
        (Integer. (re-find #"\d+" (.toString (.getBuffer writer))))
      })))

(defn report [url]
  (try
    (let [response (timed client/request (default-request url default-timeout))]
      (str "Connection to " url " has response code " (:status response) " with response time " (:time response) "ms."))
    (catch Exception e (str "HTTP request failed: " (.getMessage e)))))
