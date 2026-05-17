package org.sports.exercise.battle.server.router;

import org.sports.exercise.battle.application.services.UserService;
import org.sports.exercise.battle.infrastructure.repositories.JDBCUserRepository;
import org.sports.exercise.battle.server.auth.AuthService;
import org.sports.exercise.battle.server.messages.JsonMessageMarshaller;
import org.sports.exercise.battle.web.common.ServiceFactory;
import org.sports.exercise.battle.web.controllers.*;

public class RouteConfig {
    public static Router createRouter(){
        Router router = new Router();

        AuthService authService = new AuthService();
        JsonMessageMarshaller jsonMessageMarshaller = new JsonMessageMarshaller();
        ServiceFactory serviceFactory = new ServiceFactory();

        UserController userController = new UserController(serviceFactory, jsonMessageMarshaller, authService);
        ScoreController scoreController = new ScoreController(serviceFactory, jsonMessageMarshaller, authService);
        StatsController statsController = new StatsController(serviceFactory, jsonMessageMarshaller, authService);
        HistoryController historyController = new HistoryController(serviceFactory, jsonMessageMarshaller, authService);
        TournamentController tournamentController = new TournamentController(serviceFactory, jsonMessageMarshaller, authService);
        AchievementController achievementController = new AchievementController(serviceFactory, jsonMessageMarshaller, authService);

        //user registering - authentication
        router.post("/users", userController::register);
        router.post("/sessions", userController::login);

        //user profile
        router.get("/users/{username}", userController::getProfile);
        router.put("/users/{username}", userController::updateProfile);

        //stats - scoreboard
        router.get("/stats", statsController::getUserStats);
        router.get("/score", scoreController::getScoreboard);

        //pushup history
        router.get("/history", historyController::getUserPushUpHistory);
        router.post("/history", historyController::addPushUpRecord);

        //tournaments
        router.get("/tournament", tournamentController::getTournamentInformation);

        //unique mandatory feature - achievements
        router.get("/achievements", achievementController::getAchievement);

        return router;
    }
}
