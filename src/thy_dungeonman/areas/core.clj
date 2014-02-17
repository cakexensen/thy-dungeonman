(ns thy-dungeonman.areas.core)

(defrecord Area [id])

(defmulti help :id)

(defmulti look :id)

(defmulti helpeth :id)
