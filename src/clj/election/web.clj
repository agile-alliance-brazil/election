(ns election.web
  (:gen-class)
  (:use org.httpkit.server)
  (:require
    [clojure.tools.logging :as log]
    [ring.middleware.reload :as reload]
    [compojure.core :refer [wrap-routes routes]]
    [compojure.route :as route]
    [hiccup.middleware :as hiccup-middleware]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
    [ring.middleware.json :as json-middleware]
    [ring.middleware.anti-forgery :as anti-forgery]
    [ring.middleware.logger :as logger]
    [taoensso.tower.ring :as tower-middleware]
    [clansi.core :as clansi]
    [ring.util.response :refer [response redirect]]
    [environ.core :refer [env]]
    [optimus.prime :as optimus]
    [optimus.assets :as assets]
    [optimus.optimizations :as optimizations]
    [optimus.strategies :as strategies]
    [election.routes.site-router :as site-router]
    [election.routes.api-router :as api-router]
    [election.i18n.messages :as i18n]
  )
)

(defn in-dev? [] (= "true" (:dev env)))

(defn get-assets []
  (concat
    (assets/load-bundles "public"
      {"application.js" ["/js/application.js", "/js/vote.js"]})
    (assets/load-bundles "public"
      {"application.css" ["/css/application.css"]})))

(defn wrap-with-logger [handler]
  (let [logging-handler (logger/wrap-with-logger handler)]
    (if (in-dev?)
      logging-handler
      (fn [request]
        (clansi/without-ansi (logging-handler request))
      )
    )
  )
)

(defn wrap-locale-in-session [handler]
  (fn [{locale :locale {session-locale :locale :as session} :session :as request}]
    (let [response (handler request)]
      (if (or (nil? locale) (= session-locale locale))
        response
        (if (not (contains? response :session))
          (assoc response :session (assoc session :locale locale))
          (assoc-in response [:session :locale] locale)
        )
      )
    )
  )
)

(def wrapped-handler
  (->
    (routes
      (-> api-router/routes
        (wrap-routes json-middleware/wrap-json-body)
        (wrap-routes json-middleware/wrap-json-response)
      )
      (-> site-router/routes
          (wrap-routes hiccup-middleware/wrap-base-url)
          (wrap-routes anti-forgery/wrap-anti-forgery)
          (wrap-locale-in-session)
          (tower-middleware/wrap-tower i18n/my-tconfig {:fallback-locale i18n/preferred-language :locale-selector i18n/select-locale})
          (wrap-defaults
            (-> site-defaults
              (assoc-in [:security :anti-forgery] false)
              (assoc-in [:session :cookie-attrs :same-site] :lax)
            )
          )
      )
    )
    (wrap-defaults api-defaults)
    (optimus/wrap get-assets
      (if (in-dev?) optimizations/none optimizations/all)
      (if (in-dev?) strategies/serve-live-assets strategies/serve-frozen-assets))
    (wrap-with-logger)
  )
)

(def handler
  (if (in-dev?)
      (reload/wrap-reload wrapped-handler) ;; only reload when dev
      wrapped-handler))

(defn -main [& [port]] ;; entry point, lein run will pick up and start from here
  (let [p (Integer. (or port (:port env) 5000))]
    (run-server handler {:port p :join? false})
  )
)
