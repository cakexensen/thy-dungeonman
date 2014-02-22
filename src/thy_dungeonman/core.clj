(ns thy-dungeonman.core
  (:gen-class)
  (:use [thy-dungeonman.command :only [make-command match-command |]]))

(defn -main
  "initialize game stuffs and start running"
  [& args]
  (let [commands (merge
                  (make-command :get-ye-flask > :get :ye "flask")
                  (make-command :get > "get" | "take")
                  (make-command :ye > "ye" | "yon" | nil)
                  (make-command :get-unknown > :get :unknown))
        matched (match-command ["get" "flusk"] commands :get-unknown)]
    (println (str "match get flusk: " matched))))
