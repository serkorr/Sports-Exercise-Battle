package org.sports.exercise.battle.server.auth;

import org.sports.exercise.battle.server.exceptions.BadRequestException;
import org.sports.exercise.battle.server.exceptions.UnauthorizedException;
import org.sports.exercise.battle.server.messages.HttpRequest;

public class AuthService {
    private final String TOKEN_SUFIX = "-sebToken";
    private final String AUTH_PREFIX = "Basic ";

    public String getAuthenticatedUsername(HttpRequest request) {
        String authorizationHeader = request.header("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith(AUTH_PREFIX)){
            throw new UnauthorizedException("Invalid authorization header");
        }

        String token = authorizationHeader.substring(AUTH_PREFIX.length());

        if(!token.endsWith(TOKEN_SUFIX)){
            throw new UnauthorizedException("Invalid token format");
        }

        String username =  token.substring(0, token.length() - TOKEN_SUFIX.length());

        if(username.isBlank()){
            throw new UnauthorizedException("Invalid token username");
        }

        return username;
    }
}
