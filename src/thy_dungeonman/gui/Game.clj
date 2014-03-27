(ns thy-dungeonman.gui.Game
  (:import [com.badlogic.gdx Game Gdx Screen]
           [com.badlogic.gdx.graphics Color GL20]
           [com.badlogic.gdx.graphics.g2d BitmapFont]
           [com.badlogic.gdx.scenes.scene2d Stage]
           [com.badlogic.gdx.scenes.scene2d.ui Label Label$LabelStyle]
           [com.badlogic.gdx.backends.lwjgl LwjglApplication]))

(gen-class :name thy-dungeonman.gui.Game
           :extends com.badlogic.gdx.Game)

(defn make-screen
  "makes a gui suitable for the game"
  []
  (let [stage (atom nil)]
    (proxy [Screen] []
      ; resize is called every time the game is resized and not paused
      (resize [w h])
      ; show
      (show []
        (reset! stage (Stage.))
        (let [style (Label$LabelStyle. (BitmapFont.) (Color. 1 1 1 1))
              label (Label. "Hello world!" style)]
          (.addActor @stage label)))
      ; hide
      (hide [])
      ; render is called in the game loop after the state updates
      (render [delta]
        (.glClearColor (Gdx/gl) 0 0 0 0)
        (.glClear (Gdx/gl) GL20/GL_COLOR_BUFFER_BIT)
        (doto @stage
          (.act delta)
          (.draw)))
      ; pause occurs just before the game is closed
      (pause [])
      ; dispose occurs just after pause
      (dispose [])
      ; resume is android only when game restores from the background
      (resume []))))

(defn -create [^Game this]
  (.setScreen this (make-screen)))
