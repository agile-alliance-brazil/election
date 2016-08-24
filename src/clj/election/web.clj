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
    [clansi.core :as clansi]
    [ring.util.response :refer [response redirect]]
    [environ.core :refer [env]]
    [optimus.prime :as optimus]
    [optimus.assets :as assets]
    [optimus.optimizations :as optimizations]
    [optimus.strategies :as strategies]
    [election.routes.site-router :as site-router]
    [election.routes.api-router :as api-router]
  )
)

(defn in-dev? [] (= "true" (:dev env)))

(defn get-assets []
  (concat
    (assets/load-bundles "public"
      {"application.js" ["/js/application.js"]})
    (assets/load-bundles "public"
      {"application.css" ["/css/application.css"]})))

(defn wrap-with-logger [handler]
  (let [logging-handler (logger/wrap-with-logger handler)]
    (if (in-dev?)
      logging-handler
      (fn [request]
        (let [response (clansi/without-ansi (logging-handler request))]
          response)))))

(def wrapped-handler
  (->
    (routes
      (-> api-router/routes
        (wrap-routes json-middleware/wrap-json-body)
        (wrap-routes json-middleware/wrap-json-response))
      (-> site-router/routes
          (wrap-routes hiccup-middleware/wrap-base-url)
          (wrap-routes anti-forgery/wrap-anti-forgery)
          (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))
    (wrap-defaults api-defaults)
    (optimus/wrap get-assets
      (if (in-dev?) optimizations/none optimizations/all)
      (if (in-dev?) strategies/serve-live-assets strategies/serve-frozen-assets))
    (wrap-with-logger)))

(def handler
  (if (in-dev?)
      (reload/wrap-reload wrapped-handler) ;; only reload when dev
      wrapped-handler))

(defn -main [& [port]] ;; entry point, lein run will pick up and start from here
  (let [p (Integer. (or port (:port env) 5000))]
    (run-server handler {:port p :join? false})))
