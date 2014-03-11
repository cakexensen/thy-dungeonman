(ns thy-dungeonman.handler
  (:use [thy-dungeonman.command :only [process-input]]))

(defn message
  "sets the message to display"
  [game & text]
  (assoc game :message (apply str text)))

(defn move
  "moves the player to a new location"
  [game location]
  (process-input "look" (assoc game :location location)))

(defn score
  "updates the game score"
  [game delta]
  (update-in game [:score] + delta))

(defn game-over
  "causes a game over"
  [game]
  (assoc game :game-over true))
