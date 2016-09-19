(ns election.views.mailer
  (:require
    [election.routes.paths :as paths]
    [election.i18n.messages :as i18n]
    [taoensso.tower :as tower]
    [clj-time.core :as t]
    [clj-time.coerce :as c]
    [clj-time.format :as f]
    [hiccup.core :as hiccup]
    [hiccup.element :as element]
  )
  (:import [java.util Locale])
)

(defn format-date [locale date]
  (f/unparse
    (-> (f/formatters :rfc822)
      (f/with-zone (t/time-zone-for-id i18n/preferred-timezone))
      (f/with-locale (Locale. (first (clojure.string/split (name locale) #"[-_]"))))
    )
    (c/from-sql-date date)
  )
)

(defn- text-token-email [{voter-name :name token :token {election-id :id deadline :enddate} :election}]
  (let [locale i18n/preferred-language]
    (i18n/t
      {:locale locale}
      :mailer/token/text
      voter-name
      (format-date locale deadline)
      (paths/url-for (paths/place-vote-path election-id token))
    )
  )
)

(defn- html-token-email [{voter-name :name token :token {election-id :id deadline :enddate} :election}]
  (let [locale i18n/preferred-language]
    (i18n/t
      {:locale locale}
      :mailer/token/html
      voter-name
      (format-date locale deadline)
      (paths/url-for (paths/place-vote-path election-id token))
      (paths/url-for (paths/place-vote-path election-id token))
    )
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
