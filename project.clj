(defproject ablibrary-stats "0.1.0"
  :description "API defined to facilitate fetching ABLibrary books statistics."
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [clj-http "3.10.0"]]
  :repl-options {:init-ns ablibrary-stats.core})
