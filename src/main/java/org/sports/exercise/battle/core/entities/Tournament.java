package org.sports.exercise.battle.core.entities;

import java.time.Instant;
import java.util.UUID;

public class Tournament {
    private UUID id;
    private TournamentStatus status;
    private Instant startedAt;
    private Instant endedAt;

    public Tournament(UUID id, Instant startedAt, Instant endedAt, TournamentStatus status) {
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

    public boolean hasFinished(){
        return Instant.now().isAfter(endedAt);
    }

    public void finish(){
        this.status = TournamentStatus.FINISHED;
    }
}
