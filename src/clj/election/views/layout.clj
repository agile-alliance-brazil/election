(ns election.views.layout
  (:require
    [hiccup.page :as page]
    [optimus.link :as link]
    [election.db.elections :as db]
  )
)

(defn layout [request content]
  (page/html5
    {:lang "en"}
    [:head
      [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]
      (map page/include-css (link/bundle-paths request ["application.css"]))]
    [:body {}
      [:div.content content]
      (map page/include-js (link/bundle-paths request ["application.js"]))]))

(defn election-layout [request content]
  (let [election (db/election (:election-id request))]
    (layout request
      [:div.election
        [:h1 (:name election)]
        content
      ]
    )
  )
)
