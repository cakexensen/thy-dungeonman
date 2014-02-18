(ns thy-dungeonman.game
  (:use [thy-dungeonman.areas.core :only [make-area]]
        [thy-dungeonman.areas.main]))

(defrecord Game [current-area areas message])

(defn new-game []
  (map->Game {:current-area :main
              :areas {:main (make-area :main)}
              :message nil}))
