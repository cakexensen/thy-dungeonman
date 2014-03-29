(ns thy-dungeonman.gui.Game
  (:import [com.badlogic.gdx Game Gdx Screen Input$Keys InputProcessor]
           [com.badlogic.gdx.graphics Color GL20]
           [com.badlogic.gdx.graphics.g2d BitmapFont]
           [com.badlogic.gdx.scenes.scene2d Stage]
           [com.badlogic.gdx.scenes.scene2d.ui Label Label$LabelStyle]
           [com.badlogic.gdx.backends.lwjgl LwjglApplication LwjglFiles]
           [com.badlogic.gdx.files FileHandle])
  (:use [thy-dungeonman.gui.state :only [message input-promise input-buffer]]))

; map inputs to characters. currently not supporting uppercase?
(def typographic-keys
  {Input$Keys/A \a
   Input$Keys/B \b
   Input$Keys/C \c
   Input$Keys/D \d
   Input$Keys/E \e
   Input$Keys/F \f
   Input$Keys/G \g
   Input$Keys/H \h
   Input$Keys/I \i
   Input$Keys/J \j
   Input$Keys/K \k
   Input$Keys/L \l
   Input$Keys/M \m
   Input$Keys/N \n
   Input$Keys/O \o
   Input$Keys/P \p
   Input$Keys/Q \q
   Input$Keys/R \r
   Input$Keys/S \s
   Input$Keys/T \t
   Input$Keys/U \u
   Input$Keys/V \v
   Input$Keys/W \w
   Input$Keys/X \x
   Input$Keys/Y \y
   Input$Keys/Z \z
   Input$Keys/SPACE \space})

; style used for all labels
; use a delay, because creating the LabelStyle at compile time doesn't work
(def style
  (delay
    (Label$LabelStyle.
     (BitmapFont. (.internal (LwjglFiles.) "courier-new-32.fnt") false)
     (Color. 0 1 0 1))))

(def max-line-length 33)

(def line-pixel-height 32)

(def first-line-y 440)

(def animated-message (atom ""))

(def animated-index (atom 0))

(defn animate-message
  "'animates' the message being written by adding one character at a time"
  [message]
  (let [message-changed? (not= message @animated-message)]
    (when message-changed?
      (swap! animated-message (fn [_] message))
      (swap! animated-index (fn [_] 0)))
    (when (< @animated-index (count @animated-message))
      (swap! animated-index inc))
    (take @animated-index @animated-message)))

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
  [message stage]
  (loop [message message
         line-number 0]
    (let [[line rest] (split-at max-line-length message)
          [line rest] (adjust-for-split-word line rest)
          line-label (Label. (apply str line) @style)]
      (.setY line-label (- first-line-y (* line-number line-pixel-height)))
      (.addActor @stage line-label)
      (when-not (empty? rest)
        (recur rest (inc line-number))))))

(defn clear-screen
  "clears the screen"
  []
  (.glClearColor (Gdx/gl) 0 0 0 0)
  (.glClear (Gdx/gl) GL20/GL_COLOR_BUFFER_BIT))

; true if we are past the title screen
(def title-shown (atom false))

(defn title-screen
  "displays the title screen"
  [stage]
  (let [title "Thy Dungeonman"
        graphic ["/\\      /\\     /\\"
                 "||/----\\||     ||"
                 "\\_------_/     ||"
                 " / o  o \\      ||"
                 " /  ||  \\   o__||__o"
                 " / ---- \\    \\----/"
                 " /\\/\\/\\/\\      ||"
                 "               oo"]
        prompt "Press a key to enter yon Dungeon"
        title-label (Label. title @style)
        title-label-x 190
        graphic-labels (map #(Label. % @style) graphic)
        graphic-labels-offsets (map vector (iterate inc 0) graphic-labels)
        graphic-labels-first-line-y (- first-line-y (* 3 line-pixel-height))
        graphic-labels-x 120
        prompt-label (Label. prompt @style)
        prompt-label-x 0]
    (.setY title-label first-line-y)
    (.setX title-label title-label-x)
    (doseq [[offset label] graphic-labels-offsets]
      (.setY label (- graphic-labels-first-line-y (* offset line-pixel-height)))
      (.setX label graphic-labels-x))
    (.setX prompt-label prompt-label-x)
    (.addActor @stage title-label)
    (doseq [label graphic-labels]
      (.addActor @stage label))
    (.addActor @stage prompt-label)))

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
        (if @title-shown
          (let [input-label (Label. (str ">" (apply str @input-buffer)) @style)
                prompt-label (Label. "What wouldst thou deau?" @style)
                partial-message (animate-message @message)]
            (format-message partial-message stage)
            (.setY prompt-label line-pixel-height)
            (.addActor @stage prompt-label)
            (.addActor @stage input-label))
          (title-screen stage))
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
       (not @title-shown) (swap! title-shown (fn [_] true))
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
