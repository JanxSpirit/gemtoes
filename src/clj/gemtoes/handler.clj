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
  {(s/optional-key :id) s/Str
   :title s/Str})

(s/defschema Maker
  {(s/optional-key :id) s/Str
   :name s/Str
   :fullname s/Str
   :country s/Str
   :min-order Long})

(def conn (mg/connect))
(def db   (mg/get-db conn "gemtoes"))

(defn clean-object-id [dbo]
  (-> dbo
      (assoc :id (str (:_id dbo)))
      (dissoc :_id)))

(defn set-object-id [id dbo]
  (-> dbo
      (assoc :_id id)
      (dissoc :id)))

(defn save-to-mongo! [id dbo collname]
  (clean-object-id (mc/save-and-return db collname (set-object-id id dbo))))

(defn get-makers [] (map clean-object-id (mc/find-maps db "makers")))

(defn get-gmtos [] (map clean-object-id (mc/find-maps db "gmtos")))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!!!"]
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
                         :return {:result [Gmto]}
                         :summary "Return list of GMTOs"
                         (response/ok {:result (get-gmtos)}))
                    (POST "/" []
                          :return {:result Gmto}
                          :body [gmto Gmto]
                          :summary "Add a GMTO"
                          (response/created
                           {:result (save-to-mongo! gmto "gmtos")})))
           (context "/makers" []
                    :tags ["makers"]
                    (GET "/" []
                         :return {:result [Maker]}
                         :summary "Return a list of Makers"
                         (response/ok {:result (get-makers)}))
                    (PUT "/:id" [id]
                          :return {:result Maker}
                          :body [maker Maker]
                          :summary "Add a Maker"
                          (response/created
                           {:result (save-to-mongo! id maker "makers")}))))

  (resources "/")
  (not-found "Not Found")
  )

(def app (wrap-middleware #'gtroutes))
