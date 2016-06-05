(ns gemtoes.views.home
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [gemtoes.views.crud :as crud :refer [crud-link crud-input]]))

(defn gmto-form
  []
  (let [current-gmto-title (subscribe [:current-gmto-title])]
    (fn []
      [:form
       [:div.form-group
        [:label {:for "gmto-name"} "GMTO Title:"]
        [:input#gmto-name.form-control {:type "text"
                                        :value @current-gmto-title
                                        :on-change (fn [e]
                                                     (dispatch [:update-current-gmto-title (-> e .-target .-value)]))}]]])))

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
             (let [id (:id gmto)]
              [:li {:key id}
               [crud-input id (:name gmto) :active-edit-gmto gmto-form]]))
           [:li [crud-input "new" "Add GMTO" :active-edit-gmto gmto-form]]]]])))
