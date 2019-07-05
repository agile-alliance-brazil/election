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
    [markdown.core :as md]
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

(defn text-candidate-partial [locale]
  (fn [{:keys [fullname minibio motivation region twitterhandle]}]
    (i18n/t
      {:locale locale}
      :mailer/reminder/candidate-partial-text
      fullname
      motivation
      region
      (if (empty? twitterhandle)
        ""
        (i18n/t
          {:locale locale}
          :mailer/reminder/candidate-social-text
          (str "https://twitter.com/" twitterhandle)
        )
      )
    )
  )
)

(defn html-candidate-partial [locale]
  (fn [{:keys [fullname minibio motivation region twitterhandle]}]
    (i18n/t
      {:locale locale}
      :mailer/reminder/candidate-partial-html
      fullname
      (md/md-to-html-string motivation)
      region
      (if (empty? twitterhandle)
        ""
        (i18n/t
          {:locale locale}
          :mailer/reminder/candidate-social-html
          (str "twitter.com/" twitterhandle)
          (str "https://twitter.com/" twitterhandle)
        )
      )
    )
  )
)

(defn- text-reminder-email [{voter-name :fullname} {:keys [candidates enddate name candidatestoelect]}]
  (let [locale i18n/preferred-language]
    (i18n/t
      {:locale locale}
      :mailer/reminder/text
      voter-name
      (count candidates)
      (i18n/t {:locale locale} :mailer/token/subject name)
      (format-date locale enddate)
      (clojure.string/join
        "\n\n"
        (map (text-candidate-partial locale) candidates)
      )
    )
  )
)

(defn- html-reminder-email [{voter-name :fullname} {:keys [candidates enddate name candidatestoelect]}]
  (let [locale i18n/preferred-language]
    (i18n/t
      {:locale locale}
      :mailer/reminder/html
      voter-name
      (count candidates)
      (i18n/t {:locale locale} :mailer/token/subject name)
      (format-date locale enddate)
      (clojure.string/join
        "\n\n"
        (map (html-candidate-partial locale) candidates)
      )
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

(defn election-reminder-email-body [{election :election :as voter}]
  [
    :alternative
    {
      :type "text/plain"
      :content (text-reminder-email voter election)
    }
    {
      :type "text/html"
      :content (html-reminder-email voter election)
    }
  ]
)
