(ns election.i18n.messages
  (:require
    [environ.core :refer [env]]
    [taoensso.tower :as tower :refer-macros (with-tscope)]
  )
)

(def supported-languages [:pt-BR :en-US])

(def preferred-language (first supported-languages))

(def my-tconfig
  {
    :dictionary
    (reduce
      merge
      {}
      (map
        (fn [l]
          {
            l
            (-> (str l)
              (clojure.string/replace #"-" "_")
              (clojure.string/replace #":" "")
              (str ".clj")
            )
          }
        )
        supported-languages
      )
    )
    :dev-mode? (= "true" (:dev env)) ; Set to true for auto dictionary reloading
    :fallback-locale preferred-language
  }
)

(def translations (tower/make-t my-tconfig))

(defn t [{locale :locale} & args]
  (apply
    translations
    (conj
      args
      (apply
        vector
        (cons
          (or (keyword locale) preferred-language)
          supported-languages
        )
      )
    )
  )
)

(defn select-locale [{{locale-params :locale} :params {locale-session :locale} :session :as request}]
  (or locale-params locale-session preferred-language)
)
