(ns gemtoes.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [register-sub]]))

(register-sub
 :display-page
 (fn [db]
   (reaction (:display-page @db))))

(register-sub
 :set-focus
 (fn [db]
   (reaction (:focus-element-id @db))))

;; makers
(register-sub
 :makers
 (fn [db]
   (reaction (:makers @db))))

(register-sub
 :active-edit-maker
 (fn [db]
   (reaction (:active-edit-maker @db))))

;; gmtos
(register-sub
 :gmtos
 (fn [db]
   (reaction (:gmtos @db))))

(register-sub
 :active-edit-gmto
 (fn [db]
   (reaction (:active-edit-gmto @db))))

(defn register-nested-crud-properties!
  [model properties]
  (doseq [prop properties]
    (let [db-model (keyword (str "current-" model))
          sub-name (keyword (str "current-" model "-" prop))]
      (register-sub
       sub-name
       (fn [db]
         (reaction (get-in @db [db-model (keyword prop)])))))))

(register-nested-crud-properties!
 "gmto"
 ["title"])

(register-nested-crud-properties!
 "maker"
 ["name" "fullname" "country" "min-order"])
