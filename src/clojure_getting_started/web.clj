(ns clojure-getting-started.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            [camel-snake-kebab.core :as kebab]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<html><body><h1>Hier gibt's noch nix zu gucken, "
              "<a href='http://2048game.com'>geh spielen ...</a></h1>"
              "</body></html>")})

(defroutes app
  (GET "/camel" {{input :input} :params}
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body (kebab/->CamelCase input)})

  (GET "/snake" {{input :input} :params}
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body (kebab/->snake_case input)})

  (GET "/kebab" {{input :input} :params}
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body (kebab/->kebab-case input)})

  (GET "/" []
       (splash))

  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 3000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
