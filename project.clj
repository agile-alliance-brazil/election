(let [version (str "1.0.0_" (or (System/getenv "BUILD_NUMBER") "SNAPSHOT"))]
  (defproject election version
    :description "An election clojure server"
    :url "https://aab-election.herokuapps.com"
    :license {:name "MIT"}
    :min-lein-version "2.0.0"
    :source-paths ["src/clj"]
    :test-paths ["test/clj"]
    :main election.web
    :aot [clojure.tools.logging.impl election.web election.clocked-actions.clock]
    :dependencies [[org.clojure/clojure "1.8.0"]
      [org.clojure/data.zip "0.1.2"]
      [org.clojure/data.xml "0.0.8"]
      [org.clojure/data.json "0.2.6"]
      [org.clojure/data.csv "0.1.3"]
      [org.clojure/tools.logging "0.3.1"]
      [javax.servlet/javax.servlet-api "3.1.0"]
      [compojure "1.5.1"]
      [ring/ring-defaults "0.2.1"]
      [ring/ring-core "1.5.0"]
      [ring/ring-devel "1.5.0"]
      [cheshire "5.6.3"]
      [ring/ring-json "0.4.0" :exclusions [cheshire]]
      [ring.middleware.logger "0.5.0"]
      [http-kit "2.2.0"]
      [environ "1.1.0"]
      [clj-http "3.3.0"]
      [hiccup "1.0.5"]
      [ragtime "0.6.3"]
      [org.postgresql/postgresql "9.4.1211"]
      [com.mchange/c3p0 "0.9.5.2"]
      [honeysql "0.8.1"]
      [clj-time "0.12.2"]
      [optimus "0.19.0"]
      [digest "1.4.5"]
      [com.draines/postal "2.0.1"]
      [com.taoensso/tower "3.1.0-beta5"]
      [slingshot "0.12.2"]
      [clojurewerkz/quartzite "2.0.0" :exclusions [c3p0]]
      [markdown-clj "0.9.90"]]
    :plugins [[lein-environ "1.0.2"] [lein-pprint "1.1.2"]]
    :ring
      { :handler election.web/handler
        :uberwar-name ~(str "election-with-dependencies_" version ".war")}
    :uberjar {:aot :all}
    :uberjar-name ~(str "election-standalone_" version ".jar")
    :profiles {
      :production
      {:env {:production "true" :clj-env "production"}}
      :test
      { :plugins [[com.jakemccrary/lein-test-refresh "0.16.0"]
          [lein-cloverage "1.0.6"]
          [lein-dotenv "RELEASE"]]
        :resource-paths ["resources" "test/resources/"]
        :dependencies [[pjstadig/humane-test-output "0.8.1"]]
        :env {:test "true" :clj-env "test"}
        :injections [(require 'pjstadig.humane-test-output)
          (pjstadig.humane-test-output/activate!)]}
      :interactive {
        :dependencies [[clj-livereload "0.2.0"]]
        :injections [(require 'clj-livereload.server)
          (clj-livereload.server/start!
            {
              :paths ["resources/", "src/clj/election"]
              :debug? true
            }
          )
        ]
      }
      :dev
      { :plugins [[lein-dotenv "RELEASE"] [lein-cooper "1.2.2"]]
        :env {:development "true" :clj-env "development"}
        :cooper {"test"     ["lein" "with-profile" "base,test" "test-refresh"]
                 "server"   ["lein" "with-profile" "base,dev,interactive" "run"]
                 "clock"    ["lein" "run" "-m" "election.clocked-actions.clock"]}}
      :repl
      { :plugins [[lein-dotenv "RELEASE"]]
        :env {:development "true" :clj-env "development"}
        :dependencies [[org.clojure/tools.nrepl "0.2.12"]]
        :resource-paths ["resources"]}}
    :aliases {"migrate"  ["run" "-m" "election.db.migration/migrate"]
              "rollback" ["run" "-m" "election.db.migration/rollback"]}))
