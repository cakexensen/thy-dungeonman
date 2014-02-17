(ns thy-dungeonman.core
  (:use [thy-dungeonman.areas.core :only [look]]
        [thy-dungeonman.areas.main :only [make-main]])
  (:gen-class))

(defn -main
  "initialize game stuffs and start running"
  [& args]
  (println (look (make-main))))
