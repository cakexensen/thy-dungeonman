(ns thy-dungeonman.core
  (:gen-class)
  (:use [thy-dungeonman.command :only [make-command match-command |]]))

(defn -main
  "initialize game stuffs and start running"
  [& args]
  (let [commands (merge
                  (make-command :get-ye-flask > :get :ye "flask")
                  (make-command :get > "get" | "take")
                  (make-command :ye > "ye" | "yon" | nil))
        matched (match-command ["get" "flask"] commands :get-ye-flask)]
    (println (str "match get flask: " matched))))
