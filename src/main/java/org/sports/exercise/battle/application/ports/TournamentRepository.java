package org.sports.exercise.battle.application.ports;

import org.sports.exercise.battle.core.entities.Tournament;

import java.util.Optional;
import java.util.UUID;

public interface TournamentRepository {
    void save(Tournament tournament);
    Optional<Tournament> findById(UUID id);
    Optional<Tournament> findRunningTournament();
    void update(Tournament tournament);
    void deleteById(UUID id);
}
