(ns thy-dungeonman.gui.core
  (:import [com.badlogic.gdx Game Gdx Graphics Screen]
           [com.badlogic.gdx.graphics Color GL20]
           [com.badlogic.gdx.graphics.g2d BitmapFont]
           [com.badlogic.gdx.scenes.scene2d Stage]
           [com.badlogic.gdx.scenes.scene2d.ui Label Label$LabelStyle]
           [com.badlogic.gdx.backends.lwjgl LwjglApplication]))

; message-area is the label or whatever is holding the text log
; !! replace with the actual control in the gui
(def message-area (atom ""))

; input-promise blocks the game process until an input occurs
(def input-promise (atom (promise)))

; !! just transplant the body into the actual event handler
(defn input-event
  "occurs when user presses enter after entering text"
  []
  (deliver @input-promise "whatever the user entered"))

(defn display-message
  "adds a message to the message area of the screen"
  [message]
  ; update the text in the message area
  ; !! use the appropriate function for editing the text area
  (swap! message-area #(concat % "\n" message)))

(defn get-input
  "gets the next input from the user"
  []
  (let [input @@input-promise] ; deref atom and then promise
    ; reset input-promise to be a new promise now that it has been delivered
    (swap! input-promise #(promise))
    ; return the input
    input))

(defn start
  "starts the gui and runs the game"
  [run-game]
  ; set up the screen and such here
  ;(run-game display-message get-input)
  ; or if we need a separate thread for this, use
  ;(.start (Thread. #(run-game display-message get-input)))
  (LwjglApplication. (thy-dungeonman.gui.Game.) "Thy Dungeonman" 800 600 true)
  )
