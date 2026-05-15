package org.sports.exercise.battle.infrastructure.repositories;

import org.sports.exercise.battle.application.ports.PushUpRecordRepository;
import org.sports.exercise.battle.core.entities.PushUpRecord;
import org.sports.exercise.battle.infrastructure.database.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JDBCPushUpRecordRepository implements PushUpRecordRepository {
    private final Connection connection;

    public JDBCPushUpRecordRepository(Connection connection){
        this.connection = connection;
    }
    @Override
    public void save(PushUpRecord pushUpRecord) {
        String sql = """
                INSERT INTO push_up_records (id, user_id, tournament_id, name, count, duration_in_seconds, trained_at)
                VALUES(?, ?, ?, ?, ?, ?, ?)
                """;
        try(Connection connection = DatabaseConfig.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, pushUpRecord.getId().toString());
            preparedStatement.setString(2, pushUpRecord.getUserId().toString());
            preparedStatement.setString(3, pushUpRecord.getTournamentId().toString());
            preparedStatement.setString(4, pushUpRecord.getName());
            preparedStatement.setInt(5, pushUpRecord.getCount());
            preparedStatement.setInt(6, pushUpRecord.getDurationInSeconds());
            preparedStatement.setString(7, pushUpRecord.getTrainedAt().toString());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException("Could not add pushup record to database", e);
        }
    }

    @Override
    public Optional<PushUpRecord> findById(UUID id) {
        String sql = """
                SELECT id, user_id, tournament_id, name, count, duration_in_seconds, trained_at
                FROM push_up_records
                WHERE id = ?
                """;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, id.toString());

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return Optional.of(convertToPushUpRecord(resultSet));
                }

                return Optional.empty();
            }

        }catch(SQLException e){
            throw new RuntimeException("Could not find push up record for id: " + id.toString(), e);
        }
    }

    @Override
    public List<PushUpRecord> findByUserId(UUID user_id) {
        String sql = """
                SELECT id, user_id, tournament_id, name, count, duration_in_seconds, trained_at
                FROM push_up_records
                WHERE user_id = ?
                """;

        List<PushUpRecord> pushUpRecords = new ArrayList<>();

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, user_id.toString());

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    pushUpRecords.add(convertToPushUpRecord(resultSet));
                }

                return pushUpRecords;
            }
        }catch(SQLException e){
            throw new RuntimeException("Could not find push up records for user_id: " + user_id.toString(), e);
        }
    }

    @Override
    public List<PushUpRecord> findByTournamentId(UUID tournament_id) {
        String sql = """
                SELECT id, user_id, tournament_id, name, count, duration_in_seconds, trained_at
                FROM push_up_records
                WHERE tournament_id = ?
                """;

        List<PushUpRecord> pushUpRecords = new ArrayList<>();

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, tournament_id.toString());

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    pushUpRecords.add(convertToPushUpRecord(resultSet));
                }

                return pushUpRecords;
            }
        }catch(SQLException e){
            throw new RuntimeException("Could not find push up records for tournament: " + tournament_id.toString(), e);
        }
    }

    private PushUpRecord convertToPushUpRecord(ResultSet resultSet) throws SQLException {
        return new PushUpRecord(
                UUID.fromString(resultSet.getString("id")),
                UUID.fromString(resultSet.getString("user_id")),
                UUID.fromString(resultSet.getString("tournament_id")),
                resultSet.getString("name"),
                resultSet.getInt("count"),
                resultSet.getInt("duration_in_seconds"),
                Instant.parse(resultSet.getString("trained_at"))
        );

    }
}
