(ns gemtoes.views.crud
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]))

(defn crud-link
  [id name event]
  [:a {:href "#"
       :on-click #(dispatch [event id])}
   name]
  )

(defn crud-input
  [id name active-id-subs form]
  (let [active-id (subscribe [active-id-subs])]
    (fn []
      (if (= id @active-id)
        [form]
        [crud-link id name active-id-subs]))))
