(ns thy-dungeonman.game
  (:use [thy-dungeonman.areas.main :only [make-main]]) )

(defrecord Game [current-area areas message])

(defn new-game []
  (map->Game {:current-area :main
              :areas {:main (make-main)}
              :message nil}))
