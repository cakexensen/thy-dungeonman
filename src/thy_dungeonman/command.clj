(ns thy-dungeonman.command)

(defmulti command
  "commands dispatch by the area they are issued in, and by the words"
  (fn [area & words]
    (apply vector (:id area) words)))

; generic commands and defaults
(defmethod command :default [area & words]
  (cond
   (and (= (first words) "look") (seq? (rest words))) "It looketh pretty awesome."
   :else "That does not computeth. Type HELP if thou needs of it."))
