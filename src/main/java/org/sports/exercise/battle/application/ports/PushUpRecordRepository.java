package org.sports.exercise.battle.application.ports;


import org.sports.exercise.battle.core.entities.PushUpRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PushUpRecordRepository {
    void save(PushUpRecord pushUpRecord);
    Optional<PushUpRecord> findById(UUID id);
    List<PushUpRecord> findByUserId(UUID user_id);
    List<PushUpRecord> findByTournamentId(UUID tournament_id);
}