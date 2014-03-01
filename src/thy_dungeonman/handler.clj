(ns thy-dungeonman.handler)

(defn message
  "sets the message to display"
  [game text]
  (assoc game :message text))
