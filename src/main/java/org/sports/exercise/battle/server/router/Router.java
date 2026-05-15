package org.sports.exercise.battle.server.router;

import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;

import java.util.HashMap;
import java.util.Map;


public class Router {

    private final Map<RouteKey, RouteHandler> routes = new HashMap<>();

    public void get(String path, RouteHandler handler){
        routes.put(new RouteKey("GET", path), handler);
    }

    public void post(String path, RouteHandler handler){
        routes.put(new RouteKey("POST", path), handler);
    }

    public void delete(String path, RouteHandler handler){
        routes.put(new RouteKey("DELETE", path), handler);
    }

    public void put(String path, RouteHandler handler){
        routes.put(new RouteKey("PUT", path), handler);
    }

    // Finds the matching route for the request method and path
    public HttpResponse route(HttpRequest httpRequest){
        String method = httpRequest.method().toUpperCase();
        String path = httpRequest.path();

        //looks for the registered handler for this method and path combination
        RouteHandler handler = routes.get(new RouteKey(method, path));

        if(handler == null){
            return HttpResponse.notFound("{\"error\":\"Route not found\"}");
        }

        for(Map.Entry<RouteKey, RouteHandler> route : routes.entrySet()){
            RouteKey routeKey = route.getKey();

            //Skip if the methods doesnt match
            if(!routeKey.method().equals(method)){
                continue;
            }

            if(match(routeKey.path(), httpRequest.path())){
                //delegate the request to the corresponding controller endpoint
                return route.getValue().handle(httpRequest);
            }
        }
        return HttpResponse.notFound("{\"error\":\"Route not found\"}");
    }

    //in order to handle dynamic path parameters we need a helper function
    private boolean match(String routePath, String requestPath) {
        String[] routeParts = routePath.split("/");
        String[] requestParts = requestPath.split("/");

        if (routeParts.length > requestParts.length) {
            return false;
        }

        for (int i = 0; i < routeParts.length; ++i) {
            String routePart = routeParts[i];
            String requestPart = requestParts[i];

            if (routePart.startsWith("{") && routePart.endsWith("}")) {
                continue;
            }

            if (!routePart.equals(requestPart)) {
                return false;
            }
        }
        return true;
    }
}
