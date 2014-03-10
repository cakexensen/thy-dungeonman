(ns thy-dungeonman.areas.north
  (:use [thy-dungeonman.areas.core :only [->Area]]
        [thy-dungeonman.command :only [make-command]]
        [thy-dungeonman.handler :only [message move score]]))

(def commands
  (merge (make-command :look-parapets -> "look" :ye "parapets")
         (make-command :look-rope -> "look" :ye "rope")
         (make-command :get-rope -> :get :ye "rope")
         (make-command :go-south -> "go" "south")))

(def handlers
  {:help (fn [game unknowns]
           (message game "You go NORTH through yon corridor. You arrive at parapets. Ye see a rope. Obvious exits are SOUTH."))
   :look-parapets (fn [game unknowns]
                    (message game "Well, they're parapets. This much we know for sure."))
   :look-rope (fn [game unknowns]
                (message game "It looks okay. You've seen better."))
   :get-rope (fn [game unknowns]
               (-> game
                   (score -1)
                   (message "You attempt to take ye ROPE but alas it is enchanted! It glows a mustard red and smells like a public privy. The ROPE wraps round your neck and hangs you from parapets. With your last breath, you wonder what parapets are. GAME OVER. Your score was: " (dec (:score game)) ". Play again? (Y/N)")))
   :go-south (fn [game unknowns]
               (move game :main))})

(defn make-north
  "create the north room"
  []
  (->Area :north command handlers))
