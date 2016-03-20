(ns gemtoes.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [ring.util.http-response :as response]
            [hiccup.page :refer [include-js include-css html5]]
            [gemtoes.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]
            [monger.core :as mg]
            [monger.collection :as mc]))

(def conn (mg/connect))
(def db   (mg/get-db conn "gemtoes"))

(defn save-gmto [gmto] (mc/insert-and-return db "gmtos" gmto))
(defn save-maker [maker] (mc/insert-and-return db "makers" maker))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(def loading-page
  (html5
   [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))]
    [:body
     mount-target
     (include-js "/js/app.js")]))


(defroutes routes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)
  (GET "/admin" [] loading-page)

  (GET "/api/gmtos" [] (response/ok {:body [{"name" "value"} {"thing1" "red" "thing2" "blue" "thing3" ["a" "b" "c"]}]}))
  (POST "/api/gmtos" request (response/ok (str (save-gmto (request :params)))))
  (POST "/api/makers" request (response/ok (str (save-maker (request :params)))))

  (resources "/")
  (not-found "Not Found")
  )

(def app (wrap-middleware #'routes))
