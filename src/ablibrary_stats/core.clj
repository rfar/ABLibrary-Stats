(ns ablibrary-stats.core
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [clojure.math.numeric-tower :as math]
            [ablibrary-stats.api-utils :as api]))

(defn find-smallest-pdf []
  (->> (api/fetch-book-sizes)
       (remove (comp zero? :pdf_size))
       (apply min-key :pdf_size)))

(defn sort-pdfs-by-size []
  (->> (api/fetch-book-sizes)
       (remove (comp zero? :pdf_size))
       (sort-by :pdf_size)
       (take 5)))

(defn get-unique-field-values [field num]
  (->> (api/fetch-book-info-fields [:id field])
       (remove (comp str/blank? str/trim str #(get % field "")))
       (take num)))

(defn report-books-text-pdf-count []
  (let [books-info (api/fetch-book-info-fields [:id :has_text :attachment :verified])
        total-count (count books-info)
        with-pdf-count (count (filter :attachment books-info))
        without-text-count (count (remove :has_text books-info))
        not-verified-count (count (remove :verified books-info))]
    ;; (println "Books count:" total-count)
    (printf "Not verified Books: %5d (%.2f%%)\n" not-verified-count (* 100 (float (/ not-verified-count total-count))))
    (printf "Books with PDF: \t%5d (%.2f%%)\n" with-pdf-count (* 100 (float (/ with-pdf-count total-count))))
    (printf "Books without Text: %5d (%.2f%%)\n" without-text-count (* 100 (float (/ without-text-count total-count))))))

;;---------------------------------------------------------------------------------

;; (def BOOK_FILE_TYPES {:pdf "pdf_size" :abx "abx_size"})

(def GIGABYTE (math/expt 2.0 20))

(defn total-books-size [type]
  (/ (reduce +
             (map #(get % type)
                  (api/fetch-book-sizes)))
     GIGABYTE))

(defn print-books-stats []
  (let [total-pdf-size (total-books-size :pdf_size)
        total-abx-size (total-books-size :abx_size)]
    (printf "\nTotal PDF sizes: %.2f GB\n" total-pdf-size)
    (printf "Total ABX sizes: %.2f GB\n" total-abx-size)
    (printf "                 --------\n" total-abx-size)
    (printf "                 %.2f GB\n" (+ total-pdf-size  total-abx-size))))

(ablibrary-stats.core/find-smallest-pdf)
(ablibrary-stats.core/sort-pdfs-by-size)
(ablibrary-stats.core/print-books-stats)
