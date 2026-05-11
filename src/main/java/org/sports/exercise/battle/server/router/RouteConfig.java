package org.sports.exercise.battle.server.router;

import org.sports.exercise.battle.web.controllers.UserController;

public class RouteConfig {
    public static Router createRouter(){
        Router router = new Router();

        UserController userController = new UserController();

        router.post("/users", userController::register);
        router.get("/users/test", userController::test);
        return router;
    }
}
