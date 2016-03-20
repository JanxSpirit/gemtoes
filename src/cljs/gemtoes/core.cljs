(ns gemtoes.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [gemtoes.config :as config]
            [gemtoes.handlers]
            [gemtoes.subs]
            [gemtoes.db :as db]
            [gemtoes.views :as views]
            [ajax.core :refer [GET]]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init! []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))

;;admin API GET calls
(defn get-makers []
  (GET "/api/makers" []
       {:response-format :json
        :keywords? true
        :handler #(re-frame/dispatch [:update-makers [:makers %]])}))
