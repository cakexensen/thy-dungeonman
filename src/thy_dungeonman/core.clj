(ns thy-dungeonman.core
  (:gen-class)
  (:use [thy-dungeonman.command :only [process-input]]
        [thy-dungeonman.game :only [new-game]]))

(defn run-game
  "runs the game process and recurses with state changes"
  [write-line read-line]
  (loop [game (new-game)]
    (write-line (:message game))
    (let [input (read-line)]
      (when (not= input "exit")
        (recur (process-input input game))))))

(defn -main
  "starts the game"
  [& args]
  (run-game println read-line))
