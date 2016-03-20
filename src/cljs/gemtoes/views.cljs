(ns gemtoes.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]))

(defn newmakerinput []
  (let [new-maker-active (subscribe [:new-maker-active])]
    (fn []
      (if @new-maker-active
        [:input {:type "text"}]
        [:a {:href "/admin"} "Add new maker"]))))

(defn admin-page []
  [:div [:h2 "Gemtoes Admin"]
   (newmakerinput)])

(defn main-panel []
  (let [new-maker-active (subscribe [:new-maker-active])]
    (fn []
      [:div [:h2 "Gemtoes Admin"]
      (if @new-maker-active
        [:input {:type "text"}]
        [:a {:href "#"
             :on-click #(dispatch [:activate-new-maker])}
         "Add new maker"])])))

