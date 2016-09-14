(ns election.views.status
  (:require
    [clojure.tools.logging :as log]
    [hiccup.page :as page]
    [optimus.link :as link]
    [clj-http.client :as client]
    [election.db.status :as db-status]
    [election.connection.status :as connection-status]
    [election.i18n.messages :as i18n]
  )
)

; TODO: Improve UI. Show errors first, failures after and OK systems last.
; Also provide overall system health summary
(defn render-view [request]
  (let [connection (connection-status/report request connection-status/default-request-url)]
    (page/html5
      {:lang "en"}
      [:head
        [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]]
      [:body {}
        [:p (i18n/t request :status/up)]
        [:p (db-status/report request)]
        [:p connection]
      ]
    )
  )
)
