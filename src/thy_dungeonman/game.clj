(ns thy-dungeonman.game
  (:use [thy-dungeonman.areas.main :only [make-main]]))

(defrecord Game [location areas message])

(defn new-game
  "initializes a new game"
  []
  (->Game :main
          {:main (make-main)}
          ""))
