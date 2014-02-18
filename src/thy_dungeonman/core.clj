(ns thy-dungeonman.core
  (:use [thy-dungeonman.command :only [command]]
        [thy-dungeonman.game :only [new-game]])
  (:gen-class))

(defn -main
  "initialize game stuffs and start running"
  [& args]
  (println (:message (command (new-game) "look"))))
