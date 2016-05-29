(ns gemtoes.handler
  (:require [compojure.route :refer [not-found resources]]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ring.util.http-response :as response]
            [hiccup.page :refer [include-js include-css html5]]
            [gemtoes.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :as mconvert]))

(s/defschema Gmto
  {:title s/Str})

(def conn (mg/connect))
(def db   (mg/get-db conn "gemtoes"))

(defn clean-object-id [dbo]
  (-> dbo
      (assoc :id (str (:_id dbo)))
      (dissoc :_id)))

(defn save-gmto [gmto] 
  (clean-object-id (mc/insert-and-return db "gmtos" gmto)))

(defn save-maker [maker] 
  (clean-object-id (mc/insert-and-return db "makers" maker)))

(defn get-makers [] (let [res (map #(dissoc % :_id) (mc/find-maps db "makers"))]
                      (println res)
                      res
                      ))

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
    [:meta {:name    "viewport"
            :content "width=device-width, initial-scale=1"}]
     (include-css (if (env :dev) "/css/bootstrap.css" "/css/bootstrap.min.css"))]
    [:body
     mount-target
     (include-js "/js/app.js")
     (include-js "/js/jquery.js")
     (include-js "/js/bootstrap.min.js")]))

(defroutes gtroutes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)
  (GET "/admin" [] loading-page)

  ;; makers
  (context "/api" []
           :tags ["api"]
           (context "/gmtos" []
                    :tags ["gmtos"]
                    (GET "/" []
                         :return {:result Gmto}
                         :summary "Return list of GMTOs"
                         (response/ok {:result {:title "My GMTO"}}))
                    (POST "/" []
                          :return s/Str
                          :body [gmto Gmto]
                          :summary "Add a GMTO"
                          (response/created (save-gmto gmto))))
          #_ (context "/makers"
                      (GET "/" [] (response/ok {:body (get-makers)}))
                    (POST "/" request (response/created (str (save-maker (request :params)))))))

  (resources "/")
  (not-found "Not Found")
  )

(def app (wrap-middleware #'gtroutes))


