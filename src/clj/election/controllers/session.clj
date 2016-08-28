(ns election.controllers.session
  (:use [slingshot.slingshot :only [try+]])
  (:require
    [environ.core :refer [env]]
    [clj-http.util :as http-util]
    [clj-http.client :as http]
    [ring.util.response :refer [redirect]]
    [election.routes.paths :as paths]
    [election.java-bridge :as bridge]
  )
)

(def identity-base (:identity-host env))
(def my-host (:host env))

(def oauth2-params
  {
    :client-id (:identity-client-id env)
    :client-secret (:identity-client-secret env)
    :authorize-uri (str identity-base "/oauth/authorize")
    :redirect-uri (str my-host "/auth/aab-identity/callback")
    :access-token-uri (str identity-base "/oauth/token")
  }
)

(defn- authorize-uri [client-params csrf-token]
  (str
    (:authorize-uri client-params)
    "?response_type=code"
    "&client_id="
    (http-util/url-encode (:client-id client-params))
    "&redirect_uri="
    (http-util/url-encode (:redirect-uri client-params))
    ; "&scope="
    ; (http-util/url-encode (:scope client-params))
    "&state="
    (http-util/url-encode csrf-token)
  )
)

(defn- get-authentication-response [csrf-token response-params]
  (if (= csrf-token (:state response-params))
    ; (try
      (->
        (http/post
          (:access-token-uri oauth2-params)
          {
            :form-params
            {
              :code (:code response-params)
              :grant_type "authorization_code"
              :client_id (:client-id oauth2-params)
              :redirect_uri (:redirect-uri oauth2-params)
            }
            :basic-auth
            [(:client-id oauth2-params) (:client-secret oauth2-params)]
            :as :json
          }
        )
        :body
      )
      ; (catch Exception _ nil)
    ; )
    nil
  )
)

(defn- refresh-tokens
  "Request a new token pair"
  [refresh-token]
  (try+
    (let [{{access-token :access_token refresh-token :refresh_token} :body}
          (http/post
            (:access-token-uri oauth2-params)
            {
              :form-params
              {
                :grant_type "refresh_token"
                :refresh_token refresh-token
              }
              :basic-auth
              [(:client-id oauth2-params) (:client-secret oauth2-params)]
              :as :json})]
      [access-token refresh-token]
    )
    (catch [:status 401] _ nil)
  )
)

(defn get-user-info
  "User info API call"
  [access-token refresh-token]
  (try+
    (let [url (str identity-base "/users/me.json")]
      (->
        (http/get url {:oauth-token access-token, :as :json})
        :body
      )
    )
    (catch [:status 401] _
      (if (nil? refresh-token)
        nil
        (get-user-info (first (refresh-tokens refresh-token)) nil)
      )
    )
  )
)

(defn new-session [_]
  (let [csrf (bridge/random-uuid)]
    (->
      (redirect (authorize-uri oauth2-params csrf))
      (assoc-in [:session :state] csrf)
    )
  )
)

(defn destroy-session [_]
  (->
    (redirect (paths/home-path))
    (assoc :flash {:type :notice :message "Logged out successfully!"})
    (assoc :session nil)
  )
)

(defn login [{{code :code state :state :as params} :params session :session :as request}]
  (let [response (get-authentication-response (:state session) params)]
    (if (nil? response)
      (->
        (destroy-session request)
        (assoc :flash {:type :error :message "Invalid authentication. Please try again"})
      )
      (->
        (redirect (paths/home-path))
        (assoc :flash {:type :notice :message "Logged in successfully!"})
        (assoc-in [:session :user]
          (merge
            (get-user-info (:access_token response) (:refresh_token response))
            {
              :access-token (:access_token response)
              :refresh-token (:refresh_token response)
            }
          )
        )
      )
    )
  )
)
