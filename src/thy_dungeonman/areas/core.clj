(ns thy-dungeonman.areas.core)

(defrecord Area [id])

(defmulti command
  "commands dispatch by the area they are issued in, and by their first word"
  (fn [area first-word & rest-words] [(:id area) first-word]))
