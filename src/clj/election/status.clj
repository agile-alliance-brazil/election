(ns election.status
  (:require
    [clojure.tools.logging :as log]
    [hiccup.page :as page]
    [optimus.link :as link]
    [clj-http.client :as client]
    [clojure.java.jdbc :as j]
    [election.db.config :as db-config]
    [honeysql.core :as sql]))

(defn- hide-password [jdbc-map]
  (merge jdbc-map {:password "hidden"}))

(defn db-status []
  (try
    (let [query { :select [:id] :from [:ragtime_migrations] :order-by [[:id :desc]] :limit 1 }
      result (j/query db-config/dbspec (sql/format query))
      last-migration (:id (first result))
      migration-name (or last-migration "none.")]
      (str "up. Last migration: " migration-name))
    (catch Exception e
      (str "Connection/query failed to "
        (hide-password db-config/jdbc-config) ": "
        e (.getMessage e)))))

(def default-request-url
  "https://www.google.com")

(def default-timeout 1000)

(defn default-request [url timeout]
  {:method :get :url url :socket-timeout timeout :conn-timeout timeout :throw-exceptions false})

(defn timed [op & args]
  (let [writer (java.io.StringWriter.)]
    (merge
      (binding [*out* writer]
        (time (apply op args)))
      {
        :time
        (Integer. (re-find #"\d+" (.toString (.getBuffer writer))))
      })))

(defn- url-status [url]
  (try
    (let [response (timed client/request (default-request url default-timeout))]
      (str "Connection to " url " has response code " (response :status) " with response time " (response :time) "ms."))
    (catch Exception e (str "HTTP request failed: " (.getMessage e)))))
      

(defn render-view [request]
  (let [status (url-status default-request-url)]
    (page/html5
      {:lang "en"}
      [:head
        [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]]
      [:body {}
        [:p "Application is up."]
        [:p "DB connection is " (db-status)]
        [:p status]])))
