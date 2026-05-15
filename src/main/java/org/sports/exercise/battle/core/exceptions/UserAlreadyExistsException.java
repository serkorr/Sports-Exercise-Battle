package org.sports.exercise.battle.core.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String username) {
        super("User already exists: " + username);
    }
}
