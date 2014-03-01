(ns thy-dungeonman.command)

; commands are given in a context-free grammar format
;   :get-ye-flask -> :get :ye "flask"
;   :get -> "get" | "take"
;   :ye -> "ye" | "yon" | nil
; :keywords are used as non-terminal symbols, "strings" are terminal symbols

(defmacro make-command
  "converts a pretty representation of a cfg rule into processable data"
  [symbol _ & productions]
  (let [rules (partition-by #(= '| %) productions)
        rules (filter #(not= '(|) %) rules)]
    `{~symbol '~rules}))

(defn match
  [match? num-known unknowns]
  ; there's definitely an easier way to do this...
  {:match? match? :num-known num-known :unknowns unknowns})

(defn is-non-terminal?
  [symbol]
  (keyword? symbol))

(defn best-command
  [commands]
  (last (sort-by :num-known (filter :match? commands))))

(declare parse-command)

(defn expand
  [input [top-symbol rest-symbols] commands num-known unknowns]
  (best-command (map #(parse-command input
                                     (concat % rest-symbols)
                                     commands
                                     num-known
                                     unknowns)
                     (get commands top-symbol))))

(defn parse-command
  ([input rule commands] (parse-command input [rule] commands 0 []))
  ([input symbol commands num-known unknowns]
     (let [top-symbol (first symbol)
           top-input (first input)]
       (cond
        (and (empty? input) (empty? symbol)) (match true num-known unknowns)
        (= top-symbol nil) (parse-command input
                                          (rest symbol)
                                          commands
                                          num-known
                                          unknowns)
        (= top-symbol :unknown) (parse-command (rest input)
                                               (rest symbol)
                                               commands
                                               num-known
                                               (conj unknowns top-symbol))
        (= top-symbol top-input) (parse-command (rest input)
                                                (rest symbol)
                                                commands
                                                (inc num-known)
                                                unknowns)
        (is-non-terminal? top-symbol) (expand input
                                              symbol
                                              commands
                                              num-known
                                              unknowns)
        :else (match false num-known unknowns)))))
