(ns thy-dungeonman.game
  (:use [thy-dungeonman.areas.main :only [make-main]]
        [thy-dungeonman.command :only [make-command]]))

(defrecord Game [location dictionary areas commands handlers message])

(def dictionary
  (merge (make-command :get -> "get" | "take")
         (make-command :ye -> "ye" | "yon" | nil)
         (make-command :smell -> "smell" | "sniff")
         (make-command :help -> "help" | "helpeth" | "look")))

(def areas {:main (make-main)})

(def commands {})

(def handlers {})

(defn new-game
  "initializes a new game"
  []
  (->Game :main
          dictionary
          areas
          commands
          handlers
          ""))
