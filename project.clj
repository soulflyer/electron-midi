(defproject electron-midi "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.439"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.6"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [org.clojure/core.async "0.4.490"]
                 [re-com "2.4.0"]
                 [re-pressed "0.2.2"]]

  :plugins [[lein-cljsbuild "1.1.7"]]
  :min-lein-version "2.5.3"
  :source-paths ["src/clj" "src/cljs"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]
  :figwheel {:css-dirs    ["resources/public/css"]
             :server-port 3434}
  :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.3.6"]
                   [day8.re-frame/tracing "0.5.1"]
                   [figwheel-sidecar "0.5.18"]
                   [cider/piggieback "0.3.10"]]

    :plugins [[lein-figwheel "0.5.16"]]}
   :prod { :dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :incremental  true
     :assert       true
     :compiler     {:output-to      ".out/app/electron-host.js"
                    :output-dir     ".tmp/app"
                    ;;:asset-path    "lib/ui"
                    :pretty-print   true
                    :output-wrapper true
                    :elide-asserts  true
                    :target         :nodejs
                    :optimizations  :simple}}

    {:id           "dev-ui"
     :source-paths ["src-ui/cljs"]
     :incremental  true
     :assert       true
     :figwheel     {:on-jsload "electron-midi.core/mount-root"}
     :compiler     {:main                 electron-midi.core
                    :output-to            ".out/app/ui.js"
                    :output-dir           ".out/app/public/lib/ui"
                    :elide-asserts        true
                    :optimizations        :none
                    :pretty-print         true
                    :output-wrapper       true
                    :asset-path           "lib/ui"
                    :preloads             [devtools.preload
                                           day8.re-frame-10x.preload]
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_"        true
                                           "day8.re_frame.tracing.trace_enabled_QMARK_" true}
                    :external-config      {:devtools/config {:features-to-install :all}}
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            electron-midi.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}]})
