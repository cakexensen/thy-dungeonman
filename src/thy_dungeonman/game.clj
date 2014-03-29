(ns thy-dungeonman.game
  (:use [thy-dungeonman.areas.main :only [make-main]]
        [thy-dungeonman.areas.north :only [make-north]]
        [thy-dungeonman.areas.south :only [make-south]]
        [thy-dungeonman.areas.dennis :only [make-dennis]]
        [thy-dungeonman.command :only [make-command process-input]]
        [thy-dungeonman.handler :only [message score game-over]]))

(defrecord Game [location dictionary areas commands handlers game-over-commands game-over-handlers message score unknown])

; dictionary is commands that might be used in any area
(def dictionary
  (merge (make-command :get -> "get" | "take")
         (make-command :ye -> "ye" | "yon" | nil)
         (make-command :help -> "help" | "helpeth" | "look")))

(def areas {:main (make-main)
            :north (make-north)
            :south (make-south)
            :dennis (make-dennis)})

(def commands
  (merge (make-command :die -> "die")
         (make-command :dance -> "dance")
         (make-command :get-unknown -> :get :unknown)
         (make-command :get-dagger -> :get "dagger")
         (make-command :go-unknown -> "go" :unknown)
         (make-command :look-unknown -> "look" :unknown)
         (make-command :talk-unknown -> "talk" :unknown)
         (make-command :give-unknown -> "give" :unknown)
         (make-command :smell -> "smell" | "sniff")
         (make-command :exit -> "exit" | "quit")))

(defn unknown
  [game unknowns]
  (message game "That does not computeth. Type HELP if thou needs of it."))

(def handlers
  {:die (fn [game unknowns]
          (-> game
              (score -100)
              (message "That wasn't very smart. Your score was "
                       (- (:score game) 100)
                       ". Play again? [Y/N]")
              (game-over)))
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
   :unknown unknown
   :talk-unknown (fn [game unknowns]
                   (message game "Who is " (apply str unknowns) "? Your new boyfriend? Somebody from work you don't want me to meeteth?"))
   :give-unknown (fn [game unknowns]
                   (message game "Thou don'tst have a " (apply str unknowns) " to give. Go back to your tiny life."))
   :smell (fn [game unknowns]
            (message game "You smell a Wumpus."))
   :exit (fn [game unknowns]
           nil)})

(def game-over-commands
  (merge (make-command :y -> "y" | "yes")
         (make-command :n -> "n" | "no" | "exit" | "quit")))

(declare new-game)

(def game-over-handlers
  {:y (fn [game unknowns]
        (new-game))
   :n (fn [game unknowns]
        nil)})

(defn new-game
  "initializes a new game"
  []
  ; immediately look to pre-populate the message
  (process-input "look" (->Game :main
                                dictionary
                                areas
                                commands
                                handlers
                                game-over-commands
                                game-over-handlers
                                ""
                                0
                                unknown)))
