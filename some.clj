(ns log-first.core
	 (:require [clojure.java.io :as io]
             [taoensso.timbre :as timbre]
				     [taoensso.timbre.appenders.core :as appenders])
  (:gen-class))

(println "Hello, World Furutaka!")

; Set up the name of the log output file and delete any contents from previous runs (the
; default is to continually append all runs to the file).
(def log-file-name "log.txt")
;(io/delete-file log-file-name :quiet)

(timbre/refer-timbre) ; set up timbre aliases

; The default setup is simple console logging.  We with to turn off console logging and
; turn on file logging to our chosen filename.
;(timbre/set-config! [:appenders :standard-out   :enabled?] false)
;(timbre/set-config! [:appenders :spit           :enabled?] true)
;(timbre/set-config! [:shared-appender-config :spit-filename] log-file-name)

;; Disable logging to the console in timbre v4.0.0+:
(timbre/merge-config! {:appenders {:println {:enabled? false}}})

;; Create a "spit to file" appender in timbre v4.0.0+: 
(timbre/merge-config! {:appenders {:spit (appenders/spit-appender {:fname "log.txt"})}})

; Set the lowest-level to output as :debug
(timbre/set-level! :debug)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  
  ; Demonstrate logging with Timbre
  (trace "Hell, Timbre! trace")  ; will not be logged, below current log-level
  (debug "Hell, Timbre! debug")
  (info  "Hell, Timbre! info")
  (warn  "Hell, Timbre! warn")
  (error "Hell, Timbre! error")
  (fatal "Hell, Timbre! fatal")
  
   ; Demonstrate 3 arities of spy
  (info "Arg-1")
  (info "Arg-1" :Arg-2)
  (info "Arg-1" :Arg-2 ["Arg-3"] )
  (info "Arg-1" :Arg-2 ["Arg-3"] {:Arg 4} )
  
    ; Demonstrate 3 arities of spy
  (assert (= {:a 1}     (spy :info "Spy returns the last value" {:a 1} )))
  (assert (= 42         (spy (* 6 7) ))) ; no level implies :debug
  (assert (= 42         (spy :warn (* 6 7))))
  (assert (= {:a 1}     (spy :error "optional message" {:a 1} )))

  ; Even exceptions look nice in the logs
  (error (Exception. "Doh!") "Any extra" :items {:go "here"} )
  
))
