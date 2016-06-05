(ns gemtoes.db)

(def empty-maker {:name ""
                  :fullname ""
                  :country ""
                  :min-order 1})

(def empty-gmto {:title ""
                 :summary ""
                 :maker ""
                 :last ""
                 :style ""
                 :orders-count-needed 0
                 :comitted []
                 :canonical-listing ""
                 :r-goodyearwelt-link ""
                 :styleforum-link ""})

(def default-db
  {:makers []
   :gmtos []
   :current-maker empty-maker
   :current-gmto empty-gmto
   :active-edit-maker ""
   :active-edit-gmto ""
   :display-page :home
   :focus-element-id ""
   :makers-loading? false
   :gmtos-loading? false})
