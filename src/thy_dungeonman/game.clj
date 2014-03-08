(ns thy-dungeonman.game
  (:use [thy-dungeonman.areas.main :only [make-main]]
        [thy-dungeonman.command :only [make-command]]
        [thy-dungeonman.handler :only [message score]]))

(defrecord Game [location dictionary areas commands handlers message])

(def dictionary
  (merge (make-command :get -> "get" | "take")
         (make-command :ye -> "ye" | "yon" | nil)
         (make-command :smell -> "smell" | "sniff")
         (make-command :help -> "help" | "helpeth" | "look")))

(def areas {:main (make-main)})

(def commands
  (merge (make-command :die -> "die")
         (make-command :dance -> "dance")
         (make-command :get-unknown -> :get :unknown)
         (make-command :get-dagger -> :get "dagger")
         (make-command :go-unknown -> "go" :unknown)
         (make-command :look-unknown -> "look" :unknown)
         (make-command :talk-unknown -> "talk" :unknown)
         (make-command :give-unknown -> "give" :unknown)))

(def handlers
  {:die (fn [game unknowns]
          (-> game
              (score -100)
              (message "That wasn't very smart. Your score was "
                       (:score game)
                       ". Play again? [Y/N]")))
   :dance (fn [game unknowns]
            (message game "Thou shaketh it a little, and it feeleth all right."))
   :get-unknown (fn [game unknowns]
                  (message game "Thou cannotst get that. Quit making stuffeth up!"))
   :get-dagger (fn [game unknowns]
                 (-> game
                     (message "Yeah, okay.")
                     (score 25)))
   :go-unknown (fn [game unknowns]
                 (message game "Thou cannotst go there. Who do you think thou art, a magistrate?!"))
   :look-unknown (fn [game unknowns]
                   (message game "It looketh pretty awesome."))
   :unknown (fn [game unknowns]
              (message game "That does not computeth. Type HELP if thou needs of it."))
   :talk-unknown (fn [game unknowns]
                   (message game "Who is " (apply str unknowns) "? Your new boyfriend? Somebody from work you don't want me to meeteth?"))
   :give-unknown (fn [game unknowns]
                   (message game "Thou don'tst have a " (apply str unknowns) " to give. Go back to your tiny life."))
   :smell (fn [game unknowns]
            (message game "You smell a Wumpus."))})

(defn new-game
  "initializes a new game"
  []
  (->Game :main
          dictionary
          areas
          commands
          handlers
          ""))
