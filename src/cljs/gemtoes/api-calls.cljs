(ns gemtoes.api-calls
  (:require [ajax.core :as ajax :refer [GET POST]]
            [re-frame.core :as re-frame :refer [dispatch]]))

;;admin API GET calls
(defn get-makers []
  (GET "/api/makers"
       {:response-format :json
        :keywords? true
        :handler #(dispatch [:update-makers (:body %)])}))

(defn post-maker [maker]
  (POST "/api/makers"
        {:format :json
         :keywords? true
         :handler #(dispatch [:get-makers])
         :params maker}))
