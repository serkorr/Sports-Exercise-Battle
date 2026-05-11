package org.sports.exercise.battle.server.router;

public record RouteKey(String method, String path){

    public RouteKey{
        method = method.toUpperCase();
    }

}