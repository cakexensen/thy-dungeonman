(ns thy-dungeonman.areas.main
  (:use [thy-dungeonman.areas.core :only [->Area command]]))

(defn make-main []
  (->Area :main))

(defmethod command [:main "look"] [area & words]
  "Ye find yeself in yon dungeon. Ye see a SCROLL. Behind ye scroll is a FLASK. Obvious exits are NORTH, SOUTH and DENNIS.")
