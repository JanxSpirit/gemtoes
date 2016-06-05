(ns gemtoes.handlers
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame :refer [register-handler]]
            [gemtoes.db :as db]
            [gemtoes.api-calls :as api-calls :refer [get-makers
                                                     put-maker
                                                     delete-maker
                                                     get-gmtos]]))

(register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(register-handler
 :display-page
 (fn [db [_ page]]
   (assoc db :display-page page)))

(register-handler
 :update-makers
 (fn [db [_ makers]]
   (assoc db :makers makers :makers-loading? false)))

(register-handler
 :active-edit-maker
 (fn [db [_ id]]
   (let [maker (first (filter #(= id (:id %)) (:makers db)))]
     (assoc db :current-maker maker :active-edit-maker id))))

(register-handler
 :active-edit-gmto
 (fn [db [_ id]]
   (let [gmto (first (filter #(= id (:id %)) (:gmtos db)))]
     (assoc db :current-gmto gmto :active-edit-gmto id))))

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

;; gmtos
(register-handler
 :get-gmtos
 (fn [db [_]]
   (get-gmtos)
   (assoc db :gmtos-loading? true)))

(defn register-nested-crud-string-properties!
  [model properties]
  (doseq [prop properties]
    (let [db-model (keyword (str "current-" model))
          handler-name (keyword (str "update-current-" model "-" prop))]
      (register-handler
       handler-name
       (fn [db [_ p]]
         (assoc-in db [db-model (keyword prop)] p))))))

(register-nested-crud-string-properties!
 "gmto"
 ["title" "summary" "maker" "last" "style" "orders-count-needed"])

(register-nested-crud-string-properties!
 "maker"
 ["name" "fullname" "country"])
