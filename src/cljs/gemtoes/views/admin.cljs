(ns gemtoes.views.admin
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [gemtoes.views.crud :as crud :refer [crud-link crud-input]]))

(defn maker-form
  []
  (let [current-maker-name (subscribe [:current-maker-name])
        current-maker-fullname (subscribe [:current-maker-fullname])
        current-maker-country (subscribe [:current-maker-country])
        current-maker-min-order (subscribe [:current-maker-min-order])]
    (fn []
      [:form
       [:div.form-group
        [:label {:for "new-maker-name"} "Short Name:"]
        [:input#new-maker-name.form-control {:type "text"
                                             :value @current-maker-name
                                             :on-key-press (fn [e]
                                                             (if (= 13 (.-charCode e))
                                                               (dispatch [:save-maker])))
                                             :on-change (fn [e]
                                                          (dispatch [:update-current-maker-name (-> e .-target .-value)]))}]]
       [:div.form-group
        [:label {:for "new-maker-fullname"} "Full Name:"]
        [:input#new-maker-fullname.form-control {:type "text"
                                                  :value @current-maker-fullname
                                                  :on-change (fn [e]
                                                               (dispatch [:update-current-maker-fullname (-> e .-target .-value)]))}]]

       [:div.form-group
        [:label {:for "new-maker-country"} "Country:"]
        [:input#new-maker-country.form-control {:type "text"
                                             :value @current-maker-country
                                             :on-change (fn [e]
                                                          (dispatch [:update-current-maker-country (-> e .-target .-value)]))}]]
       [:div.form-group
        [:label {:for "new-maker-min-order"} "Minimum Order:"]
        [:input#new-maker-min-order.form-control {:type "text"
                                             :value @current-maker-min-order
                                             :on-change (fn [e]
                                                          (dispatch [:update-current-maker-min-order (-> e .-target .-value)]))}]]

       [:div.form-group
        [:button.btn.btn-default {:type "submit"
                                  :on-click (fn [e]
                                              (dispatch [:save-maker]))} "Save"]
        [:button.btn.btn-default {:on-click (fn [e]
                                              (dispatch [:active-edit-maker ""]))} "Cancel"]
        [:button.btn.btn-default {:on-click (fn [e]
                                              (dispatch [:delete-maker]))} "Delete"]]])))

(defn admin-panel
  []
  (let [makers (subscribe [:makers])]
    (fn []
      [:div
         [:a {:on-click #(dispatch [:display-page :home])} "home"]
         [:h2 "Gemtoes Admin"]
         [:ul
          (for [maker @makers]
            (let [id (:id maker)]
              [:li {:key id}
               [crud-input id (:name maker) :active-edit-maker maker-form]]))
          [:li [crud-input "new" "Add new maker" :active-edit-maker maker-form]]]])))
