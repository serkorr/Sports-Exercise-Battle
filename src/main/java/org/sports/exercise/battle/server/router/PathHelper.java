package org.sports.exercise.battle.server.router;

import org.sports.exercise.battle.server.exceptions.BadRequestException;

public class PathHelper {
    public static String extractUsernameFromPath(String path) {
        String prefix = "/users/";

        if (path == null || !path.startsWith(prefix)) {
            throw new BadRequestException("Invalid user path");
        }

        String username = path.substring(prefix.length());

        if (username.isBlank() || username.contains("/")) {
            throw new BadRequestException("Invalid username in path");
        }

        return username;
    }
}
