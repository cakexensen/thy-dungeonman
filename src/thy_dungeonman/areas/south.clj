(ns thy-dungeonman.areas.south
  (:use [thy-dungeonman.areas.core :only [->Area]]
        [thy-dungeonman.command :only [make-command]]
        [thy-dungeonman.handler :only [message move score]]))

(def commands
  (merge (make-command :look-trinket -> "look" :ye "trinket")
         (make-command :get-trinket -> :get :ye "trinket")
         (make-command :go-north -> "go" "north")))

(def handlers
  {:help (fn [game unknowns]
           (let [get-trinket-count (get-in game [:areas :south :get-trinket-count])]
             (cond
              (= get-trinket-count 1) (message game "Ye stand high above a canyon-like depression. Obvious exits are NORTH.")
              (= get-trinket-count 2) (message game "Thou hangeth out at an overlook. Obvious exits are NORTH. I shouldn't have to tell ye there is no TRINKET.")
              :else (message game "Ye stand yeself close to a yet-unnamed escarpment. Nonetheless, ye spies a TRINKET. Obvious exits are NORTH."))))
   :look-trinket (fn [game unknowns]
                   (if (get-in game [:areas :south :get-trinket-count])
                     (message game "Just a bulge in thou pouchel at this point.")
                     (message game "Quit looking! Just get it already.")))
   :get-trinket (fn [game unknowns]
                  (if (get-in game [:areas :south :get-trinket-count])
                    (-> game
                        (message "Sigh. The trinket is in thou pouchel. Recallest thou?")
                        (score -1)
                        (update-in [:areas :south :get-trinket-count] inc))
                    (-> game
                        (message "Ye getsts yon TRINKET and discover it to be a bauble. You rejoice at your good fortune. You shove the TRINKET in your pouchel. It kinda hurts.")
                        (score 2)
                        (assoc-in [:areas :south :get-trinket-count] 1))))
   :go-north (fn [game unknowns]
               (move game :main))})

(defn make-south
  "makes the south room"
  []
  (->Area :south commands handlers))
