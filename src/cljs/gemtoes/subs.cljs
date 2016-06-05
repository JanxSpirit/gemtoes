(ns gemtoes.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [register-sub]]))

(register-sub
 :display-page
 (fn [db]
   (reaction (:display-page @db))))

(register-sub
 :makers
 (fn [db]
   (reaction (:makers @db))))

(register-sub
 :current-maker-name
 (fn [db]
   (reaction (get-in @db [:current-maker :name]))))

(register-sub
 :current-maker-fullname
 (fn [db]
   (reaction (get-in @db [:current-maker :fullname]))))

(register-sub
 :current-maker-country
 (fn [db]
   (reaction (get-in @db [:current-maker :country]))))

(register-sub
 :current-maker-min-order
 (fn [db]
   (reaction (get-in @db [:current-maker :min-order]))))

(register-sub
 :set-focus
 (fn [db]
   (reaction (:focus-element-id @db))))

(register-sub
 :active-edit-maker
 (fn [db]
   (reaction (:active-edit-maker @db))))

;; gmtos
(register-sub
 :gmtos
 (fn [db]
   (reaction (:gmtos @db))))
