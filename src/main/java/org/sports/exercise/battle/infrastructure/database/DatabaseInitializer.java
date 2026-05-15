package org.sports.exercise.battle.infrastructure.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

//inorder to create the necessary tables
public class DatabaseInitializer {

    public static void initialize(Connection connection){
        createTournamentsTable(connection);
        createUsersTable(connection);

        //create push up records table last because of foreign key restriction
        createPushUpRecordsTable(connection);
    }

    private static void createUsersTable(Connection connection){
        String createStatement = """
                CREATE TABLE IF NOT EXISTS users(
                    id TEXT PRIMARY KEY,
                    username TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    name TEXT,
                    bio TEXT,
                    image TEXT,
                    elo INTEGER NOT NULL
                );
                """;

        executeStatement(connection, createStatement);
    }

    private static void createTournamentsTable(Connection connection){
        String createStatement = """
                CREATE TABLE IF NOT EXISTS tournaments(
                    id TEXT PRIMARY KEY,
                    started_at TEXT NOT NULL,
                    ended_at TEXT NOT NULL,
                    status TEXT NOT NULL
                );
                """;

        executeStatement(connection, createStatement);
    }

    private static void createPushUpRecordsTable(Connection connection){
        String createStatement = """
                CREATE TABLE IF NOT EXISTS push_up_records(
                    id TEXT PRIMARY KEY,
                    user_id TEXT NOT NULL,
                    tournament_id TEXT NOT NULL,
                    name TEXT NOT NULL,
                    count INTEGER NOT NULL,
                    duration_in_seconds INTEGER NOT NULL,
                    trained_at TEXT NOT NULL,
                    
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
                );
                """;

        executeStatement(connection, createStatement);
    }

    private static void executeStatement(Connection connection, String sql){
        try (Statement statement = connection.createStatement()){
            statement.execute(sql);
        }catch(SQLException e){
            throw new RuntimeException("Could not execute database initialization", e);
        }
    }
}
