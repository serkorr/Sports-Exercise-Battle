package org.sports.exercise.battle.core.entities;

import java.time.Instant;
import java.util.UUID;

public class Tournament {
    private UUID id;
    private TournamentStatus status;
    private Instant startedAt;
    private Instant endedAt;

    public Tournament(UUID id, Instant startedAt, TournamentStatus status, Instant endedAt) {
        this.id = id;
        this.startedAt = startedAt;
        this.status = status;
        this.endedAt = endedAt;
    }

    public UUID getId() {
        return id;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }
}
