(ns thy-dungeonman.core
  (:gen-class)
  (:use [thy-dungeonman.command :only [make-command parse-command]]))

(defn -main
  "initialize game stuffs and start running"
  [& args]
  (let [commands (merge
                  (make-command :get-ye-flask -> :get :ye "flask")
                  (make-command :get -> "get" | "take")
                  (make-command :ye -> "ye" | "yon" | nil)
                  (make-command :get-unknown -> :get :unknown))
        match (parse-command ["get"] :get commands)]
    (println (str "match get: " match))))
