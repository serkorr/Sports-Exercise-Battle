package org.sports.exercise.battle.infrastructure.repositories;

import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.core.entities.User;
import org.sports.exercise.battle.core.exceptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JDBCUserRepository implements UserRepository {
    private final Connection connection;

    public JDBCUserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User user) {
        String sql = """
            INSERT INTO users (id, username, password_hash, name, bio, image, elo)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getId().toString());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setString(4, user.getName());
            preparedStatement.setString(5, user.getBio());
            preparedStatement.setString(6, user.getImage());
            preparedStatement.setInt(7, user.getElo());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not save user: " + user.getUsername(), e);
        }
    }

    @Override
    public Optional<User> find(UUID id) {
        String sql = """
            SELECT id, username, password_hash, name, bio, image, elo
            FROM users
            WHERE id = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(convertToUser(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find user: " + id, e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = """
            SELECT id, username, password_hash, name, bio, image, elo
            FROM users
            WHERE username = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(convertToUser(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find user: " + username, e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = """
                SELECT id, username, password_hash, name, bio, image, elo
                FROM users
                """;

        List<User> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(convertToUser(resultSet));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Could not find users", e);
        }
    }

    @Override
    public void update(User user) {
        String sql = """
            UPDATE users
            SET username = ?, password_hash = ?, name = ?, bio = ?, image = ?, elo = ?
            WHERE id = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getBio());
            preparedStatement.setString(5, user.getImage());
            preparedStatement.setInt(6, user.getElo());
            preparedStatement.setString(7, user.getId().toString());

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 0) {
                throw new RuntimeException("User not found: " + user.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not update user: " + user.getId(), e);
        }
    }

    @Override
    public User findByUsernameOrThrowNotFound(String username) {
        return this.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public void deleteById(UUID id) {
        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, id.toString());

            int affectedRow = preparedStatement.executeUpdate();

            //check if this statement actually changed a row
            if(affectedRow == 0){
                throw new RuntimeException("User not found: " + id.toString());
            }

        }catch(SQLException e){
            throw new RuntimeException("Could not delete user: " + id.toString(), e);
        }
    }

    private User convertToUser(ResultSet resultSet) throws SQLException {
        return new User(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("username"),
                resultSet.getString("password_hash"),
                resultSet.getString("name"),
                resultSet.getString("bio"),
                resultSet.getString("image"),
                resultSet.getInt("elo")
        );
    }
}
