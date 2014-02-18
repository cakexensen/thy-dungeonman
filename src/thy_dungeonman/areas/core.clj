(ns thy-dungeonman.areas.core)

(defrecord Area [id])

(defmulti make-area identity)
