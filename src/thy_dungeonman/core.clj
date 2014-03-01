(ns thy-dungeonman.core
  (:gen-class)
  (:use [thy-dungeonman.command :only [parse-command]]
        [thy-dungeonman.game :only [new-game]]))

(defn -main
  "initialize game stuffs and start running"
  [& args]
  (let [game (new-game)
        location (:location game)
        input ["look" "flask"]
        rule :look-flask
        parsed (parse-command input
                              rule
                              (get-in game [:areas location :commands]))]
    (println parsed)))
