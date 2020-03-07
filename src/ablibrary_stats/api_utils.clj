(ns ablibrary-stats.api-utils
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.math.numeric-tower :as math]
            [clojure.walk :as walk]
            [clojure.string :as str]))

(def BASE-URL "https://ablibrary.net/")

(defn build-query [fields]
  (str "?fields="
       (clojure.string/join "," (map name fields))))

(defn fetch-book-info-fields [fields]
  (->> (http/get (str BASE-URL "books" (build-query fields)))
       :body
       json/read-str
       walk/keywordize-keys
       :data))

(defn fetch-book-sizes []
  (fetch-book-info-fields [:id :pdf_size :abx_size]))

(defn fetch-field-unique-vals [field]
  (->>
   (fetch-book-info-fields [field])
   (set)
   (map field)
   (sort)))

;;--------------------------------------------
(defn find-books-with-field-value [field val]
  (->>
   (fetch-book-info-fields [:id field])
   (filter #(= (str/trim (get % field)) val))
   (map :id)
   (sort)))
