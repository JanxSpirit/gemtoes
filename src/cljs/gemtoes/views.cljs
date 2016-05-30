(ns gemtoes.views
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]))


(defn new-maker-link
  []
  [:a {:href "#"
       :on-click #(dispatch [:activate-edit-maker "new"])}
   "Add new maker"]
  )

(defn focus-handler
  []
  (let [focus-element-id (subscribe [:set-focus])]
    (fn []
      (.focus (.getElementById js/document @focus-element-id)))))

(defn new-maker-form
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
                                              (dispatch [:save-maker]))} "Save"]]])))

(defn new-maker-input
  []
  (let [active-edit-maker (subscribe [:active-edit-maker])]
    (fn []
      (if (= "new" @active-edit-maker)
        [new-maker-form]
        [new-maker-link]))))

(defn main-panel
  []
  (let [makers (subscribe [:makers])]
    (fn []
      [:head
       [focus-handler]]
      [:div [:h2 "Gemtoes Admin"]
       [:ul
        (for [maker @makers]
          [:li {:key (:id maker)} (:name maker)])
        [:li
         [new-maker-input]]]])))


