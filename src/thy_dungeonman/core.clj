(ns thy-dungeonman.core
  (:gen-class)
  (:use [thy-dungeonman.command :only [process-input]]
        [thy-dungeonman.game :only [new-game]]
        [thy-dungeonman.gui.core :only [start]]))

(defn run-game
  "runs the game process and recurses with state changes"
  [display-message get-input]
  (loop [game (new-game)]
    (when-not (nil? game)
      (display-message (:message game))
      (let [input (get-input)]
        (recur (process-input input game))))))

(defn -main
  "starts the game"
  [& args]
  (start run-game)
  ;(run-game println read-line)
  )
