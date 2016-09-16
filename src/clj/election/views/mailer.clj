(ns election.views.mailer
  (:require
    [election.routes.paths :as paths]
    [election.i18n.messages :as i18n]
    [hiccup.core :as hiccup]
    [hiccup.element :as element]
  )
)

(defn- text-token-email [token]
  (i18n/t
    {:locale i18n/preferred-language}
    :mailer/token/text
    (:name token)
    (paths/url-for (paths/place-vote-path (:id (:election token)) (:token token)))
  )
)

(defn- html-token-email [token]
  (i18n/t
    {:locale i18n/preferred-language}
    :mailer/token/html
    (:name token)
    (paths/url-for (paths/place-vote-path (:id (:election token)) (:token token)))
    (paths/url-for (paths/place-vote-path (:id (:election token)) (:token token)))
  )
)

(defn election-token-email-body [token]
  [
    :alternative
    {
      :type "text/plain"
      :content (text-token-email token)
    }
    {
      :type "text/html"
      :content (html-token-email token)
    }
  ]
)
