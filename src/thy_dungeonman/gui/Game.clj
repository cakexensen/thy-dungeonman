(ns thy-dungeonman.gui.Game
  (:import [com.badlogic.gdx Game Gdx Screen Input$Keys InputProcessor]
           [com.badlogic.gdx.graphics Color GL20]
           [com.badlogic.gdx.graphics.g2d BitmapFont]
           [com.badlogic.gdx.scenes.scene2d Stage]
           [com.badlogic.gdx.scenes.scene2d.ui Label Label$LabelStyle]
           [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [com.badlogic.gdx.files FileHandle])
  (:use [thy-dungeonman.gui.state :only [message input-promise input-buffer]]
        [clojure.java.io :only [resource file]]))

(def typographic-keys
  {Input$Keys/A \A
   Input$Keys/B \B
   Input$Keys/C \C
   Input$Keys/D \D
   Input$Keys/E \E
   Input$Keys/F \F
   Input$Keys/G \G
   Input$Keys/H \H
   Input$Keys/I \I
   Input$Keys/J \J
   Input$Keys/K \K
   Input$Keys/L \L
   Input$Keys/M \M
   Input$Keys/N \N
   Input$Keys/O \O
   Input$Keys/P \P
   Input$Keys/Q \Q
   Input$Keys/R \R
   Input$Keys/S \S
   Input$Keys/T \T
   Input$Keys/U \U
   Input$Keys/V \V
   Input$Keys/W \W
   Input$Keys/X \X
   Input$Keys/Y \Y
   Input$Keys/Z \Z
   Input$Keys/SPACE \space})

(defn adjust-for-split-word
  "adjusts a split-at result so that a word isn't split across the boundary"
  [line rest]
  (if (empty? rest)
    [line rest]
    (let [line-length (count line)
          line-last-space (.lastIndexOf line \space)
          [new-line pre-rest] (split-at (inc line-last-space) line)
          new-rest (concat pre-rest rest)]
      [new-line new-rest])))

(defn format-message
  "wraps the message to fit the screen"
  [message style stage]
  (loop [message message
         line-number 0]
    (let [[line rest] (split-at 42 message)
          [line rest] (adjust-for-split-word line rest)
          line-label (Label. (apply str line) style)]
      (.setY line-label (- 560 (* line-number 32)))
      (.addActor @stage line-label)
      (when-not (empty? rest)
        (recur rest (inc line-number))))))

(defn clear-screen
  "clears the screen"
  []
  (.glClearColor (Gdx/gl) 0 0 0 0)
  (.glClear (Gdx/gl) GL20/GL_COLOR_BUFFER_BIT))

(gen-class :name thy-dungeonman.gui.Game
           :extends com.badlogic.gdx.Game)

(defn make-screen
  "makes a gui suitable for the game"
  []
  (let [stage (atom nil)]
    (proxy [Screen] []
      ; resize is called every time the game is resized and not paused
      (resize [w h])
      ; show ???
      (show [])
      ; hide ???
      (hide [])
      ; render is called in the game loop after the state updates
      (render [delta]
        (clear-screen)
        (reset! stage (Stage.))
        (let [style (Label$LabelStyle.
                     (BitmapFont. (FileHandle. (file (resource "courier-new-32.fnt")))
                                  false)
                     (Color. 1 1 1 1))
              input-label (Label. (apply str @input-buffer) style)]
          (format-message @message style stage)
          (.addActor @stage input-label))
        (doto @stage
          (.act delta)
          (.draw)))
      ; pause occurs just before the game is closed
      (pause [])
      ; dispose occurs just after pause
      (dispose [])
      ; resume is android only when game restores from the background
      (resume []))))

(defn make-input-listener
  "makes a listener for input events"
  []
  (proxy [InputProcessor] []
    (keyDown [keycode]
      (cond
       (= keycode Input$Keys/BACKSPACE) (swap! input-buffer (comp vec butlast))
       (= keycode Input$Keys/ENTER) (do
                                      (deliver @input-promise (apply str @input-buffer))
                                      (swap! input-buffer (fn [_] [])))
       (contains? typographic-keys keycode) (swap! input-buffer #(conj % (get typographic-keys keycode))))
      true)
    (keyUp [keycode] true)
    (keyTyped [character] true)
    (touchDown [x y pointer button] true)
    (touchUp [x y pointer button] true)
    (touchDragged [x y pointer] true)
    (mouseMoved [x y] true)
    (scrolled [amount] true)))

(defn -create [^Game this]
  (.setScreen this (make-screen))
  (.setInputProcessor Gdx/input (make-input-listener))
  )
