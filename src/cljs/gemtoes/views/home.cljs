(ns gemtoes.views.home
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]))

(defn gmto-input
  [id name]
  [:a name])

(defn home-panel
  []
  (let [gmtos (subscribe [:gmtos])]
      (fn []
        [:div
         [:a {:on-click #(dispatch [:display-page :admin])} "admin"]
         [:div
          [:h2 "GMTOs"]
          [:ul
           (for [gmto @gmtos]
             [:li {:key (:id gmto)}
              (:name gmto)])
           [:li [gmto-input "new" "Add GMTO"]]]]])))
