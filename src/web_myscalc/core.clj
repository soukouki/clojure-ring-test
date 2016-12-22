(ns web_myscalc.core
	(:require
		[clojure.string :as str]
		[clojure.java.shell :as sh]
		[clojure.java.io :as io]
		[ring.adapter.jetty :as server]))

(defonce server (atom nil))

(defn search-view []
	(slurp (io/file (io/resource "search.html"))))
		
(defn myscalc [f]
	(:out (sh/sh "myscalc.cmd" :in f)))

(defn calc-view [uri]
	(str (str/replace (myscalc (subs uri 1)) #"\r\n" "<br>") "<br><a href='/'>return</a>"))

(defn select-view [uri]
	(case uri
		"/" (search-view)
		(calc-view uri)))

(defn handler [req] (println (:uri req))
	{
	:status 200,
	:headers {"Content-Type" "text/html"},
	:body (select-view (:uri req))})

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
