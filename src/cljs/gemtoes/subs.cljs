(ns gemtoes.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [register-sub]]))

(register-sub
 :makers
 (fn [db]
   (reaction (:makers @db))))

(register-sub
 :new-maker-active?
 (fn [db]
   (reaction (:new-maker-active? @db))))

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
