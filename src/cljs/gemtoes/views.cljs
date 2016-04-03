(ns gemtoes.views
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]))

(defn new-maker-form
  []
  (fn [new-maker-value]
    ))

(defn new-maker-link
  []
  [:a {:href "#"
       :on-click #(dispatch [:activate-new-maker])}
        "Add new maker"]
  )

(defn new-maker-form
  []
  (let [new-maker-current-value (subscribe [:new-maker-current-value])]
    (fn []
      [:input {:type "text"
               :value @new-maker-current-value
               :on-key-press (fn [e]
                              (if (= 13 (.-charCode e))
                                (dispatch [:add-maker (-> e .-target .-value)])))
               :on-change (fn [e]
                            (dispatch [:update-new-maker-value (-> e .-target .-value)]))}])))

(defn new-maker-input
  []
  (let [new-maker-active? (subscribe [:new-maker-active?])
    (fn []
      (if @new-maker-active?
                [new-maker-form]
                [new-maker-link]))))

(defn main-panel
  []
  (let [makers (subscribe [:makers])
       maker-names (reaction (map :name @makers))]
    (fn []
      [:div [:h2 "Gemtoes Admin"]
        [:ul
          (for [maker-name @maker-names]
            [:li {:key maker-name} maker-name])
            [:li
              [new-maker-input]]]])))

