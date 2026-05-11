package org.sports.exercise.battle.server.exceptions;

public class BadRequestException extends HttpException{
    public BadRequestException(String message){
        super(message);
    }
}
