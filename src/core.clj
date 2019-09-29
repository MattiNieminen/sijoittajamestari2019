(ns core
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.java.io :as io]))

(def source-file-name "from-yahoo.csv")
(def target-file-name "for-notebook.csv")
(def day-count-for-prediction 10)

(defn data-line->coll [s]
  (let [[_ open high low _ close volume] (-> s (str/split #","))]
    [open high low close volume]))

(defn csv-line [coll]
  (str (str/join "," coll) "\n"))

(defn append-to-csv! [file-name s]
  (spit file-name (csv-line s) :append true))

(defn data-partitions [data n]
  (partition (inc n) 1 data))

(defn xs [p]
  (->> p butlast (apply concat)))

(defn current-closing-price [p]
  (->> p butlast last butlast last edn/read-string))

(defn next-closing-price [p]
  (-> p last (nth 3) edn/read-string))

(defn y [current-close next-close]
  (if (> next-close current-close) 1 0))

(defn -main []
  (println (format "Converting %s to %s" source-file-name target-file-name))
  (io/delete-file target-file-name true)
  (with-open [rdr (io/reader source-file-name)]
    (let [data-lines (-> rdr line-seq rest)
          data (map data-line->coll data-lines)
          partitions (data-partitions data day-count-for-prediction)]
      (doseq [p partitions]
        (append-to-csv! target-file-name
                        (concat (xs p) [(y (current-closing-price p)
                                           (next-closing-price p))]))))))
