(ns gemtoes.handlers
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame :refer [register-handler]]
            [gemtoes.db :as db]
            [gemtoes.api-calls :as api-calls :refer [get-makers put-maker delete-maker]]))

(register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(register-handler
 :update-makers
 (fn [db [_ makers]]
   (assoc db :makers makers :makers-loading? false)))

(register-handler
 :activate-edit-maker
 (fn [db [_ id]]
   (let [maker (first (filter #(= id (:id %)) (:makers db)))]
     (assoc db :current-maker maker :active-edit-maker id))))

(register-handler
 :save-maker
 (fn [db [_]]
   (let [newmaker (= "new" (:active-edit-maker db))]
     (if (and
          newmaker
          (some #(= (get-in db [:current-maker :name]) (:name %)) (:makers db)))
       db
       (do
         (let [maker (if newmaker
                       (assoc (:current-maker db) :id
                              (-> (get-in db [:current-maker :name])
                                  (str/trim)
                                  (str/lower-case)
                                  (str/replace " " "-")))
                       (:current-maker db))]
           (put-maker maker))
         (-> db
             (dissoc :active-edit-maker)
             (assoc :current-maker db/empty-maker)))))))

(register-handler
 :update-current-maker-name
 (fn [db [_ name]]
   (-> db
       (assoc-in [:current-maker :name] name))))

(register-handler
 :update-current-maker-fullname
 (fn [db [_ fullname]]
   (assoc-in db [:current-maker :fullname] fullname)))

(register-handler
 :update-current-maker-country
 (fn [db [_ country]]
   (assoc-in db [:current-maker :country] country)))

(register-handler
 :update-current-maker-min-order
 (fn [db [_ min-order]]
   (assoc-in db [:current-maker :min-order]
             (let [int-min-order (js/parseInt min-order)]
               (if (js/isNaN int-min-order) 0 int-min-order)))))

(register-handler
 :get-makers
 (fn [db [_]]
   (get-makers)
   (assoc db :makers-loading? true)))

(register-handler
 :delete-maker
 (fn [db [_]]
   (delete-maker (:active-edit-maker db))
   (-> db
       (assoc :current-maker db/empty-maker)
       (dissoc :active-edit-maker))))

(register-handler
 :set-element-focus
 (fn [db [_ focus-element-id]]
   (assoc db :focus-element-id focus-element-id)))
