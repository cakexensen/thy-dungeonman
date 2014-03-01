(ns thy-dungeonman.handler)

(defn message
  "sets the message to display"
  [game text]
  (assoc game :message text))

(defn move
  "moves the player to a new location"
  [game location]
  (assoc game :location location))
