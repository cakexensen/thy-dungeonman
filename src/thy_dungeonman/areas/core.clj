(ns thy-dungeonman.areas.core)

(defrecord Area [id])

(defmulti look (fn [area] (:id area)))
