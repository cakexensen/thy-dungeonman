(ns thy-dungeonman.areas.main
  (:use [thy-dungeonman.areas.core :only [->Area]]
        [thy-dungeonman.command :only [command]]))

(defn make-main []
  (->Area :main))

(defmethod command [:main "look"] [game & words]
  (assoc-in game [:message] "Ye find yeself in yon dungeon. Ye see a SCROLL. Behind ye scroll is a FLASK. Obvious exits are NORTH, SOUTH and DENNIS."))

(defmethod command [:main "look" "flask"] [game & words]
  (assoc-in game [:message] "Looks like you could quaff some serious mead out of that thing."))

(defmethod command [:main "go" "north"] [game & words]
  (assoc-in game [:current-area] :north))
