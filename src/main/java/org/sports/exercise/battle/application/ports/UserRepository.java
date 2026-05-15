package org.sports.exercise.battle.application.ports;

import org.sports.exercise.battle.core.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    Optional<User> find(UUID id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void update(User user);
    User findByUsernameOrThrowNotFound(String username);
    void deleteById(UUID id);
}
