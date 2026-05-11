package org.sports.exercise.battle.server.exceptions;

public class UnauthorizedException extends HttpException{
    public UnauthorizedException(String message){
        super(message);
    }
}
