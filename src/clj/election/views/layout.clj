(ns election.views.layout
  (:require
    [hiccup.page :as page]
    [optimus.link :as link]
    [election.db.elections :as db]
  )
)

(defn layout [{{type :type message :message} :flash :as request} content]
  (page/html5
    {:lang "en"}
    [:head
      [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]
      (map page/include-css (link/bundle-paths request ["application.css"]))]
    [:body {}
      (case type
        :error [:div.flash.error message]
        :notice [:div.flash.notice message]
        [:div.flash])
      [:div.content content]
      (page/include-js "//ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js")
      (map page/include-js (link/bundle-paths request ["application.js"]))]))

(defn election-layout [{{election-id :election-id} :params :as request} content]
  (let [election (db/election (read-string election-id))]
    (layout request
      [:div.election
        [:h1 (:name election)]
        content
      ]
    )
  )
)
