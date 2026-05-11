package org.sports.exercise.battle.server.exceptions;

public class MessageSerializationException extends RuntimeException {
    public MessageSerializationException(String message, Throwable cause){
        super(message, cause);
    }

    public MessageSerializationException(String message){
        super(message);
    }
}
