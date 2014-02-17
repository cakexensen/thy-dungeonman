(ns thy-dungeonman.areas.core)

(defrecord Area [id])

(defmulti look [area]
  (:id area))
