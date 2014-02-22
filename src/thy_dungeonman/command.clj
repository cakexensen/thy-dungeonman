(ns thy-dungeonman.command)

; commands are given in a context-free grammar format
;   :get-ye-flask > :get :ye "flask"
;   :get > "get" | "take"
;   :ye > "ye" | "yon" | nil
; :keywords are used as non-terminal symbols, "strings" are terminal symbols

(def | '|)

(defn make-command
  "converts a pretty representation of a cfg rule into processable data"
  [symbol _ & productions]
  (let [rules (filter #(not= '(|) %) (partition-by #(= | %) productions))]
    {symbol rules}))

(defn match-command
  "attempts to match an input to a command's rules"
  [input commands start]
  (letfn [(expand [input symbol stack]
            (reduce #(or %1 %2)
                    (map #(match input (concat % stack))
                         (get commands symbol))))
          (match [input stack]
            (let [empty-input? (empty? input)
                  empty-stack? (empty? stack)
                  top-input (first input)
                  top-stack (first stack)
                  rest-input (rest input)
                  rest-stack (rest stack)]
              (cond
               (and empty-input? empty-stack?) true ; both empty
               (or empty-input? empty-stack?) false ; one empty
               (nil? top-stack) (match input rest-stack) ; match empty rule
               (= top-input top-stack) (match rest-input rest-stack) ; match
               (keyword? top-stack) (expand input top-stack rest-stack) ; expand
               :else false)))]
    (match input [start])))
