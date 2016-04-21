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
 :save-maker
 (fn [db [_]]
   (if (some #(= (get-in db [:current-maker :name]) (:name %)) (:makers db))
     db
     (do
       (post-maker {:name (get-in db [:current-maker :name])
                    :fullname (get-in db [:current-maker :fullname])
                    :country (get-in db [:current-maker :country])
                    :min-order (get-in db [:current-maker :min-order])})
       (-> db
           (assoc :new-maker-active? false)
           (assoc :current-maker db/empty-maker))))))

(register-handler
 :update-current-maker-name
 (fn [db [_ name]]
   (assoc db :current-maker (assoc (:current-maker db {}) :name name))))

(register-handler
 :update-current-maker-fullname
 (fn [db [_ fullname]]
   (assoc db :current-maker (assoc (:current-maker db {}) :fullname fullname))))

(register-handler
 :update-current-maker-country
 (fn [db [_ country]]
   (assoc db :current-maker (assoc (:current-maker db {}) :country country))))

(register-handler
 :update-current-maker-min-order
 (fn [db [_ min-order]]
   (assoc db :current-maker (assoc (:current-maker db {}) :min-order min-order))))

(register-handler
 :get-makers
 (fn [db [_]]
   (get-makers)
   (assoc db :makers-loading? true)))

(register-handler
 :set-element-focus
 (fn [db [_ focus-element-id]]
   (assoc db :focus-element-id focus-element-id)))
