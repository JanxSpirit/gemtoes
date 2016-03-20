(ns gemtoes.handlers
    (:require [re-frame.core :as re-frame :refer [register-handler]]
              [gemtoes.db :as db]))

(register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(register-handler
  :update-makers
  (fn [db [_ makers]]
    (merge db makers)))

(register-handler
 :activate-new-maker
 (fn [db [_]]
   (assoc db :new-maker-active true)))
