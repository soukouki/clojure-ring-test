(ns web_myscalc.view
	(:require
		[clojure.string :as str]
		[clojure.java.shell :as sh]
		[clojure.java.io :as io]))

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
