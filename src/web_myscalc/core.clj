(ns web_myscalc.core
	(:require
		[clojure.string :as str]
		[clojure.java.shell :as sh]
		[ring.adapter.jetty :as server]))

(defonce server (atom nil))

(defn myscalc [formula]
	(:out (sh/sh "myscalc.cmd" :in (subs formula 1))))

(defn handler [req] {
	:status 200,
	:headers {"Context-Type" "text/plain"},
	:body (myscalc (req :uri))})

(defn start-server []
	(when-not @server
		(reset! server (server/run-jetty handler {:port 3000, :join? false}))))

(defn stop-server []
	(when @server
		(.stop @server)
		(reset! server nil)))

(defn restart-server []
	(when @server
		(stop-server))
	(start-server))
