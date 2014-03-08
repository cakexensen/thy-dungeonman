(ns thy-dungeonman.handler)

(defn message
  "sets the message to display"
  [game & text]
  (assoc game :message (apply str text)))

(defn move
  "moves the player to a new location"
  [game location]
  (assoc game :location location))

(defn score
  "updates the game score"
  [game delta]
  (update-in game [:score] #(+ % delta)))
