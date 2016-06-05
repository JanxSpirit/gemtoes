(ns gemtoes.views
  (:require [gemtoes.views.admin :refer [admin-panel]]
            [gemtoes.views.home :refer [home-panel]]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [reagent.core :as reagent]))

(defn main-panel
  []
  (let [display-page (subscribe [:display-page])]
    (fn []
      (case @display-page
        :admin [admin-panel]
        :home [home-panel]))))
