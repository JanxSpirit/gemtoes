(ns gemtoes.db)

(def empty-maker {:name ""
                  :fullname ""
                  :country ""
                  :min-order 1})

(def default-db
  {:makers []
   :gmtos []
   :current-maker empty-maker
   :active-edit-maker ""
   :focus-element-id ""})
