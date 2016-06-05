(ns gemtoes.views.home
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [gemtoes.views.crud :as crud :refer [crud-link crud-input]]))

(defn gmto-form
  []
  (let [current-gmto-title (subscribe [:current-gmto-title])
        current-gmto-summary (subscribe [:current-gmto-summary])
        current-gmto-maker (subscribe [:current-gmto-maker])
        current-gmto-last (subscribe [:current-gmto-last])
        current-gmto-style (subscribe [:current-gmto-style])
        current-gmto-orders-count-needed
        (subscribe [:current-gmto-orders-count-needed])
        ]
    (fn []
      [:form
       [:div.form-group
        [:label {:for "gmto-title"} "GMTO Title:"]
        [:input#gmto-name.form-control {:type "text"
                                        :value @current-gmto-title
                                        :on-change (fn [e]
                                                     (dispatch [:update-current-gmto-title (-> e .-target .-value)]))}]]
       [:div.form-group
        [:label {:for "gmto-summary"} "GMTO Summary:"]
        [:textarea#gmto-summary.form-control {:type "textarea"
                                        :value @current-gmto-summary
                                        :on-change (fn [e]
                                                     (dispatch [:update-current-gmto-summary (-> e .-target .-value)]))}]]
       [:div.form-group
        [:label {:for "gmto-maker"} "Maker:"]
        [:select#gmto-maker.form-control {:value @current-gmto-maker
                                          :on-change (fn [e]
                                                       (dispatch [:update-current-gmto-maker (-> e .-target .-value)]))}]]
       [:div.form-group
        [:label {:for "gmto-last"} "Last:"]
        [:select#gmto-last.form-control {:value @current-gmto-last
                                          :on-change (fn [e]
                                                       (dispatch [:update-current-gmto-last (-> e .-target .-value)]))}]]
       [:div.form-group
        [:label {:for "gmto-style"} "GMTO Shoe Style:"]
        [:input#gmto-style.form-control {:type "text"
                                        :value @current-gmto-style
                                        :on-change (fn [e]
                                                     (dispatch [:update-current-gmto-style (-> e .-target .-value)]))}]]
       [:div.form-group
        [:label {:for "gmto-orders-count-needed"} "Orders Count Needed:"]
        [:input#gmto-orders-count-needed.form-control {:type "text"
                                                       :value @current-gmto-orders-count-needed
                                        :on-change (fn [e]
                                                     (dispatch [:update-current-gmto-orders-count-needed (-> e .-target .-value)]))}]]
       [:div.form-group
        [:button.btn.btn-default {:type "submit"
                                  :on-click (fn [e]
                                              (dispatch [:save-gmto]))} "Save"]
        [:button.btn.btn-default {:on-click (fn [e]
                                              (dispatch [:active-edit-gmto ""]))} "Cancel"]
        [:button.btn.btn-default {:on-click (fn [e]
                                              (dispatch [:delete-gmto]))} "Delete"]]])))

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
