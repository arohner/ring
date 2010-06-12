(ns ring.middleware.flash-test
  (:use clojure.test
        ring.middleware.flash))

(deftest flash-is-added-to-session
  (let [message  {:error "Could not save"}
        handler  (wrap-flash (constantly {:flash message}))
        response (handler {:session {}})]
    (is (= (:session response) {:_flash message}))))

(deftest flash-is-retrieved-from-session
  (let [message  {:error "Could not save"}
        handler  (wrap-flash
                   (fn [request]
                     (is (= (:flash request) message))
                     {}))]
    (handler {:session {:_flash message}})))

(deftest flash-is-removed-after-read
  (let [message  {:error "Could not save"}
        handler  (wrap-flash (constantly {}))
        response (handler {:session {:_flash message}})]
    (is (nil? (:flash response)))
    (is (nil? (-> response :session :_flash)))))
