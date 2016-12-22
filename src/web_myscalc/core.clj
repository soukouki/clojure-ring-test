(ns web_myscalc.core
	(:require
		[ring.adapter.jetty :as server]
		[web_myscalc.view :as v]))

(defonce server (atom nil))

(defn handler [req] (println (:uri req))
	{
	:status 200,
	:headers {"Content-Type" "text/html"},
	:body (v/select-view (:uri req))})

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
