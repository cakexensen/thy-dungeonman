(ns thy-dungeonman.command)

(defmulti command
  "commands dispatch by the area they are issued in, and by the words"
  (fn [game & words]
    (apply vector (:current-area game) words)))

; generic commands and defaults
(defmethod command :default [game & words]
  (let [first-word (first words)
        additional-words? (seq? (rest words))]
    (cond
     (and (= first-word "look") additional-words?) "It looketh pretty awesome."
     :else "That does not computeth. Type HELP if thou needs of it.")))
