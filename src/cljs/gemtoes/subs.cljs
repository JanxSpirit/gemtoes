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
