(ns election.views.layout
  (:require
    [hiccup.page :as page]
    [hiccup.element :as e]
    [optimus.link :as link]
    [election.db.elections :as db]
    [election.routes.paths :as p]
  )
)

(defn layout [{{type :type message :message} :flash session :session :as request} content]
  (page/html5
    {:lang "en"}
    [:head
      [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]
      (map page/include-css (link/bundle-paths request ["application.css"]))]
    [:body {}
      [:div.header
        [:div.container
          [:div.logo
            [:img{:src "/images/agile-alliance.jpg"}]
          ]
          [:ul.actions]
          [:div.session
            (if (nil? (:user session))
              (e/link-to (p/login-path) "Login")
              (e/link-to (p/logout-path) (str "Logout from " (-> session :user :first_name)))
            )
          ]
        ]
      ]
      [:div.content
        (case type
          :error [:div.flash.error message]
          :notice [:div.flash.notice message]
          [:div.flash]
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
