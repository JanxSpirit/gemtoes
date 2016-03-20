(ns gemtoes.views
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]))

(defn main-panel []
  (let [new-maker-active? (subscribe [:new-maker-active?])
        makers (subscribe [:makers])
        maker-names (reaction (map :name @makers))]
    (fn []
      [:div [:h2 "Gemtoes Admin"]
        [:ul
          (for [maker-name @maker-names]
            [:li {:key maker-name} maker-name])
            [:li
              (if @new-maker-active?
                [:input {:type "text"
                         :on-key-press (fn [e]
                                 (if (= 13 (.-charCode e))
                                   (dispatch [:add-maker (-> e .-target .-value)])))}]
                [:a {:href "#"
                 :on-click #(dispatch [:activate-new-maker])}
                 "Add new maker"])]]])))

