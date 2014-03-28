(ns thy-dungeonman.gui.state)

; message contains the current message of the game
(def message (atom nil))

; input-promise blocks the game process until an input occurs
(def input-promise (atom (promise)))

; input-buffer buffers the individual typed characters until user hits enter
(def input-buffer (atom []))
