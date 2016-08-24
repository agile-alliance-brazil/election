(ns election.views.mailer
  (:require
    [election.routes.paths :as paths]
    [hiccup.core :as hiccup]
    [hiccup.element :as element]
  )
)

(defn- text-token-email [token]
  (str
    "Hello " (:name token) ",\n"
    "\n"
    "We're pleased to invite you to vote on " (:name (:election token)) ".\n"
    "\n"
    "To cast your vote, simply access the following URL. Note that you can only vote once and cannot edit your vote after placing it.\n"
    "\n"
    (paths/url-for (paths/place-vote-path (:id (:election token)) (:token token))) "\n"
    "\n"
    "Sincerely,\n"
    "Agile Alliance Brazil board"
  )
)

(defn- html-token-email [token]
  (hiccup/html
    [:p "Hello " (:name token) ","]
    [:p "We're pleased to invite you to vote on " (:name (:election token)) "."]
    [:p "To cast your vote, simply access the following URL. Note that you can only vote once and cannot edit your vote after placing it."]
    [:p (element/link-to (paths/url-for (paths/place-vote-path (:id (:election token)) (:token token))) (paths/url-for (paths/place-vote-path (:id (:election token)) (:token token))))]
    [:p 
      "Sincerely,"
      [:br]
      "Agile Alliance Brazil board"
    ]
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
  ])
