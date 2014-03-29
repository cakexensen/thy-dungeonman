(ns thy-dungeonman.gui.core
  (:import [com.badlogic.gdx Game Gdx Graphics Screen]
           [com.badlogic.gdx.graphics Color GL20]
           [com.badlogic.gdx.graphics.g2d BitmapFont]
           [com.badlogic.gdx.scenes.scene2d Stage]
           [com.badlogic.gdx.scenes.scene2d.ui Label Label$LabelStyle]
           [com.badlogic.gdx.backends.lwjgl LwjglApplication])
  (:use [thy-dungeonman.gui.state :only [message input-promise]]))

(defn display-message
  "adds a message to the message area of the screen"
  [m]
  ; update the text in the message area
  (swap! message (fn [_] m)))

(defn get-input
  "gets the next input from the user"
  []
  (let [input @@input-promise] ; deref atom and then promise
    ; reset input-promise to be a new promise now that it has been delivered
    (swap! input-promise (fn [_] (promise)))
    ; return the input
    input))

(defn start
  "starts the gui and runs the game"
  [run-game]
  (let [; set up the gui application
        app (LwjglApplication. (thy-dungeonman.gui.Game.) "Thy Dungeonman" 640 480 true)
        ; start the game in a separate thread
        game (Thread. #(do
                         (run-game display-message get-input)
                         (.stop app)))]
    (.start game))
  )
