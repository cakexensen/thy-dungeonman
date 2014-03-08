(ns thy-dungeonman.core
  (:gen-class)
  (:use [thy-dungeonman.command :only [process-input]]
        [thy-dungeonman.game :only [new-game]]))

(defn -main
  "initialize game stuffs and start running"
  [& args]
  (let [game (new-game)
        processed (process-input "talk dude" game)]
    (println (:message processed))))
