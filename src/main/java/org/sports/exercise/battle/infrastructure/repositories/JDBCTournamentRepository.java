package org.sports.exercise.battle.infrastructure.repositories;

import org.sports.exercise.battle.application.ports.TournamentRepository;
import org.sports.exercise.battle.core.entities.Tournament;
import org.sports.exercise.battle.core.entities.TournamentStatus;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class JDBCTournamentRepository implements TournamentRepository {
    private final Connection connection;

    public JDBCTournamentRepository(Connection connection){
        this.connection = connection;
    }

    @Override
    public void save(Tournament tournament) {
        String sql = """
                INSERT INTO tournaments (id, started_at, ended_at, status)
                VALUES(?, ?, ?, ?)
                """;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, tournament.getId().toString());
            preparedStatement.setString(2, tournament.getStartedAt().toString());
            preparedStatement.setString(3, tournament.getEndedAt().toString());
            preparedStatement.setString(4, tournament.getStatus().name());

            preparedStatement.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException("Could not save tournament in DB", e);
        }
    }

    @Override
    public Optional<Tournament> findById(UUID id) {
        String sql = """
                SELECT id, started_at, ended_at, status
                FROM tournaments
                WHERE id = ?
                """;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, id.toString());

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return Optional.of(convertToTournament(resultSet));
                }

                return Optional.empty();
            }
        }catch(SQLException e){
            throw new RuntimeException("Could not find tournament with id: " + id.toString(), e);
        }
    }

    @Override
    public Optional<Tournament> findRunningTournament() {
        String sql = """
                SELECT id, started_at, ended_at, status
                FROM tournaments
                WHERE status = 'RUNNING'
                ORDER BY started_at DESC
                LIMIT 1
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(convertToTournament(resultSet));
                }

                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find running tournament", e);
        }
    }

    @Override
    public void update(Tournament tournament) {
        String sql = """
                UPDATE tournaments
                SET started_at = ?, ended_at = ?, status = ?
                WHERE id = ?
                """;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, tournament.getStartedAt().toString());
            preparedStatement.setString(2, tournament.getEndedAt().toString());
            preparedStatement.setString(3, tournament.getStatus().name());
            preparedStatement.setString(4, tournament.getId().toString());

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 0) {
                throw new RuntimeException("Tournament not found: " + tournament.getId());
            }
        }catch(SQLException e){
            throw new RuntimeException("Could not update tournament with id: " + tournament.getId().toString(), e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        String sql = """
                DELETE from tournaments
                WHERE id = ?
                """;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, id.toString());

            int affectedRow = preparedStatement.executeUpdate();

            //check if this statement actually changed a row
            if(affectedRow == 0){
                throw new RuntimeException("Tournament not found: " + id);
            }

        }catch(SQLException e){
            throw new RuntimeException("Could not delete tournament with id: " + id.toString(), e);
        }
    }

    private Tournament convertToTournament(ResultSet resultSet) throws SQLException {
        return new Tournament(
                UUID.fromString(resultSet.getString("id")),
                Instant.parse(resultSet.getString("started_at")),
                Instant.parse(resultSet.getString("ended_at")),
                TournamentStatus.valueOf(resultSet.getString("status"))
                );
    }
}
