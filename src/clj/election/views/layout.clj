(ns election.views.layout
  (:require
    [hiccup.page :as page]
    [hiccup.element :as e]
    [optimus.link :as link]
    [election.db.elections :as db]
    [election.routes.paths :as p]
    [election.i18n.messages :as i18n]
    [election.routes.paths :as paths]
  )
)

(defn layout [{{type :type message :message} :flash locale :locale {user :user} :session :as request} content]
  (page/html5
    {:lang (name (or locale i18n/preferred-language))}
    [:head
      [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]
      [:meta{:name "viewport" :content "width=device-width, initial-scale=1.0"}]
      (map page/include-css (link/bundle-paths request ["application.css"]))]
    [:body {}
      [:div.header
        [:div.container
          [:div.logo
            (e/link-to (paths/path-for paths/elections-matcher) (e/image "/images/agile-alliance.jpg" (i18n/t request :agile-alliance-logo)))
          ]
          [:ul.actions]
          [:div.session
            (if (nil? user)
              (e/link-to (p/login-path) (i18n/t request :session/new))
              (e/link-to (p/logout-path) (i18n/t request :session/destroy (:first_name user)))
            )
          ]
        ]
      ]
      [:div.content
        (case type
          :error [:div.flash.error message]
          :notice [:div.flash.notice message]
          [:div]
        )
        [:div.container content]
      ]
      (page/include-js "//ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js")
      (map page/include-js (link/bundle-paths request ["application.js"]))
    ]
  )
)

(defn election-layout [{{name :name} :election :as request} content]
  (layout request
    [:div.election
      [:h1 name]
      content
    ]
  )
)
