(defproject thy-dungeonman "0.1.0-SNAPSHOT"
  :description "YOU ARE THY DUNGEONMAN!"
  :url "a website?"
  :license {:name "insert appropriate license here"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.badlogic.gdx/gdx "0.9.9-SNAPSHOT"]
                 ;[com.badlogic.gdx/gdx-natives "0.9.9-SNAPSHOT"]
                 [com.badlogic.gdx/gdx-backend-lwjgl "0.9.9-SNAPSHOT"]
                 ;[com.badlogic.gdx/gdx-backend-lwjgl-natives "0.9.9-SNAPSHOT"]
                 ;[com.badlogic.gdx/gdx-platform "0.9.9-SNAPSHOT"
                 ;:classifier "natives-desktop"]
                 ]
  :repositories [["libgdx"
                  "http://libgdx.badlogicgames.com/nightlies/maven/"]]
  :aot [thy-dungeonman.gui.Game]
  :main ^:skip-aot thy-dungeonman.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
