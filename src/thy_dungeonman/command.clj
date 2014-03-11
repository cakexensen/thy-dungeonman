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
  [input [top-symbol & rest-symbols] commands num-known unknowns]
  (best-command (map #(parse-command input
                                     (concat % rest-symbols)
                                     commands
                                     num-known
                                     unknowns)
                     (get commands top-symbol))))

(defn parse-command
  "parses an input against a specific rule"
  ([input rule commands] (parse-command input [rule] commands 0 []))
  ([input symbol commands num-known unknowns]
     (let [top-symbol (first symbol)
           top-input (first input)]
       (cond
        (and (empty? input) (empty? symbol)) (match true num-known unknowns)
        (and (not (empty? symbol))
             (= top-symbol nil)) (parse-command input
                                                (rest symbol)
                                                commands
                                                num-known
                                                unknowns)
        (= top-symbol :unknown) (parse-command (rest input)
                                               (rest symbol)
                                               commands
                                               num-known
                                               (conj unknowns top-input))
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

(defn find-best-match
  "finds the best matching command for the given input"
  [input commands]
  (let [command-symbols (keys commands)
        parse-each (fn [symbol]
                     (assoc (parse-command input symbol commands)
                       :key symbol))
        matches (map parse-each command-symbols)]
    (best-command matches)))

(defn handle-command
  "executes the specified command in the game"
  [handlers {:keys [key unknowns]} game]
  (let [handler (key handlers)]
    (handler game unknowns)))

(defn process-input
  "finds the command for the given input and executes it"
  [text game]
  (let [input (clojure.string/split text #"\s")
        game-over (get game :game-over false)
        game-over-commands (:game-over-commands game)
        best-game-over (find-best-match input game-over-commands)
        game-over-handlers (:game-over-handlers game)
        location (:location game)
        dictionary (:dictionary game)
        area (get-in game [:areas location])
        area-commands (:commands area)
        area-commands (merge area-commands dictionary)
        best-area (find-best-match input area-commands)
        area-handlers (:handlers area)]
    (cond
     game-over (handle-command game-over-handlers best-game-over game)
     (:match? best-area) (handle-command area-handlers best-area game)
     :else (let [common-commands (:commands game)
                 common-commands (merge common-commands dictionary)
                 best-common (find-best-match input common-commands)
                 common-handlers (:handlers game)]
             (handle-command common-handlers best-common game)))))
