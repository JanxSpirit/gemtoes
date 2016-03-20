(ns gemtoes.handlers
    (:require [re-frame.core :as re-frame :refer [register-handler]]
              [gemtoes.db :as db]
              [gemtoes.api-calls :as api-calls :refer [get-makers post-maker]]))

(register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(register-handler
  :update-makers
  (fn [db [_ makers]]
    (assoc db :makers makers :makers-loading? false)))

(register-handler
 :activate-new-maker
 (fn [db [_]]
   (assoc db :new-maker-active? true)))

(register-handler
 :add-maker
 (fn [db [_ maker-name]]
   (if (some #(= maker-name (:name %)) (:makers db))
     db
     (do
       (post-maker {:name maker-name})
       db))))

(register-handler
 :get-makers
 (fn [db [_]]
   (get-makers)
   (assoc db :makers-loading? true)))
