(ns thy-dungeonman.core
  (:use [thy-dungeonman.command :only [command]]
        [thy-dungeonman.areas.main :only [make-main]])
  (:gen-class))

(defn -main
  "initialize game stuffs and start running"
  [& args]
  (println (command (make-main) "look")))
