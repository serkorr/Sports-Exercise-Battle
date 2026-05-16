package org.sports.exercise.battle.web.common;

import org.sports.exercise.battle.application.common.UserDTOConverter;
import org.sports.exercise.battle.application.services.*;
import org.sports.exercise.battle.infrastructure.repositories.JDBCPushUpRecordRepository;
import org.sports.exercise.battle.infrastructure.repositories.JDBCTournamentRepository;
import org.sports.exercise.battle.infrastructure.repositories.JDBCUserRepository;

import java.sql.Connection;

public class ServiceFactory {

    public UserService createUserService(Connection connection){
        return new UserService(new JDBCUserRepository(connection), new UserDTOConverter());
    }

    public ScoreboardService createScoreboardService(Connection connection){
        return new ScoreboardService(new JDBCUserRepository(connection), new JDBCPushUpRecordRepository(connection));
    }

    public StatsService createStatsService(Connection connection){
        return new StatsService(new JDBCUserRepository(connection), new JDBCPushUpRecordRepository(connection));
    }

    public HistoryService createHistoryService(Connection connection){
        return new HistoryService(new JDBCUserRepository(connection), new JDBCPushUpRecordRepository(connection));
    }

    public TournamentService createTournamentService(Connection connection){
        return new TournamentService(new JDBCTournamentRepository(connection));
    }
}
