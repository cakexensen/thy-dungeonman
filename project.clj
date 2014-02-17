(defproject thy-dungeonman "0.1.0-SNAPSHOT"
  :description "YOU ARE THY DUNGEONMAN!"
  :url "a website?"
  :license {:name "insert appropriate license here"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :main ^:skip-aot thy-dungeonman.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
