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

(defn make-test-inputs
  "makes a reader function that will input consecutively the values in a list"
  [commands]
  (let [index (atom -1)
        commands (conj commands "exit") ; automatically exit at end
        ]
    (fn []
      (swap! index inc)
      (get commands @index))))

(defn make-test-log
  "makes a writer function that will log all of the displayed messages"
  [log]
  (fn [text] (swap! log #(conj % text))))

(deftest game-write-and-read
  (testing "write message"
    (is (let [output-stream (atom [])
              writer (make-test-log output-stream)
              reader (make-test-inputs [])]
          (run-game writer reader)
          (= (last @output-stream) "Ye find yeself in yon dungeon. Ye see a SCROLL. Behind ye scroll is a FLASK. Obvious exits are NORTH, SOUTH and DENNIS."))))
  (testing "read message"
    (is (let [output-stream (atom [])
              writer (make-test-log output-stream)
              reader (make-test-inputs ["get scroll"
                                        "look scroll"])]
          (run-game writer reader)
          (= (last @output-stream) "Ye seeth nothing wheretofore it went ZAP.")))))

(deftest game-over-conditions
  (testing "die"
    (is (let [output-stream (atom [])
              writer (make-test-log output-stream)
              reader (make-test-inputs ["die"])]
          (run-game writer reader)
          (= (last @output-stream) "That wasn't very smart. Your score was -100. Play again? [Y/N]"))))
  (testing "win"
    (is (let [output-stream (atom [])
              writer (make-test-log output-stream)
              reader (make-test-inputs ["go south"
                                        "get trinket"
                                        "go north"
                                        "go dennis"
                                        "give trinket"])]
          (run-game writer reader)
          (= (last @output-stream) "A novel idea! You givst the TRINKET to Dennis and he happily agrees to tell you what parapets are. With this new knowledge, ye escapes from yon dungeon in order to search for new dungeons and to remain... THY DUNGEONMAN!! You hath won! Congraturation!! Your score was: 2")))))
