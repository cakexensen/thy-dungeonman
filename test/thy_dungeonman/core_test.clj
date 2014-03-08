(ns thy-dungeonman.core-test
  (:require [clojure.test :refer :all]
            [thy-dungeonman.core :refer :all]
            [thy-dungeonman.game :refer [new-game]]
            [thy-dungeonman.command :refer [process-input]]))

(deftest a-new-game
  (testing "game starts in main room"
    (is (= (:location (new-game)) :main)))
  (testing "game dispatches generic command"
    (is (= (:message (process-input "look foot" (new-game)))
           "It looketh pretty awesome."))))

(deftest main-room
  (testing "look flask message in main room"
    (is (= (:message (process-input "look flask" (new-game)))
           "Looks like you could quaff some serious mead out of that thing."))))
