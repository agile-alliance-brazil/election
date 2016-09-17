(ns election.i18n.messages-test
  (:use clojure.test election.test-helper)
  (:require
    [election.i18n.messages :as i18n]
  )
)

(deftest t-test
  (testing (str "t without locale is translation of key in " i18n/preferred-language)
    (is (= "Português do Brasil" (i18n/t {} :pt-BR)))
  )
  (testing "t with known locale is translation of key in that locale"
    (is (= "Brazilian Portuguese" (i18n/t {:locale :en-US} :pt-BR)))
  )
  (testing "t with known locale is translation of key in that locale and ignores extra parameters"
    (is (= "Brazilian Portuguese" (i18n/t {:locale :en-US} :pt-BR "1")))
  )
  (testing (str "t with unknown locale is translation of key in " i18n/preferred-language)
    (is (= "Português do Brasil" (i18n/t {:locale :la-LA} :pt-BR)))
  )
  (testing (str "t with known locale that doesn't have specific key fallback to " i18n/preferred-language)
    (is (= "Cadastro de Pessoa Física" (i18n/t {:locale :en-US} :cpf)))
  )
  (testing "t with known locale that doesn't have specific key fallback to any other langauge"
    (is (= "Social Security Number" (i18n/t {:locale :pt-BR} :ssn)))
  )
  (testing "t with known locale is translation of key in that locale with parameters of correct type"
    (is (= "No election with ID 1 found." (i18n/t {:locale :en-US} :elections/not-found 1)))
  )
  (testing "t with known locale and known translation key with strong type parameter throws error"
    (is (thrown? java.util.IllegalFormatConversionException (i18n/t {:locale :en-US} :elections/not-found "1")))
  )
  (testing "t with known locale and known translation key that needs parameter without parameter is template"
    (is (= "No election with ID %d found." (i18n/t {:locale :en-US} :elections/not-found)))
  )
  (testing "t with known locale and unknown translation key is empty string"
    (is (= "" (i18n/t {:locale :en-US} :key-that/doesnt-exist)))
  )
)

(deftest select-locale-test
  (testing (str "select-locale for nil request is " i18n/preferred-language)
    (is (= i18n/preferred-language (i18n/select-locale nil)))
  )
  (testing (str "select-locale for empty request is " i18n/preferred-language)
    (is (= i18n/preferred-language (i18n/select-locale {})))
  )
  (testing (str "select-locale for request without session or params is " i18n/preferred-language)
    (is (= i18n/preferred-language (i18n/select-locale {:cookies "blabla"})))
  )
  (testing "select-locale for request without params but with session locale is session"
    (is (= "fr-FR" (i18n/select-locale {:session {:locale "fr-FR"}})))
  )
  (testing "select-locale for request with params but without session locale is params"
    (is (= "fr-FR" (i18n/select-locale {:params {:locale "fr-FR"}})))
  )
  (testing "select-locale for request with params and session locale is params"
    (is (= "fr-FR" (i18n/select-locale {:params {:locale "fr-FR"} :session {:locale "en-US"}})))
  )
)
