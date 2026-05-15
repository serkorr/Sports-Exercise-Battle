package org.sports.exercise.battle.core.entities;

import java.time.Instant;
import java.util.UUID;

public class PushUpRecord {
    private UUID id;
    private UUID userId;
    private UUID tournamentId;

    private String name;
    private int count;
    private int durationInSeconds;
    private Instant trainedAt;

    public PushUpRecord(UUID id, UUID userId, UUID tournamentId, String name, int count, int durationInSeconds, Instant trainedAt) {
        this.id = id;
        this.userId = userId;
        this.tournamentId = tournamentId;
        this.name = name;
        this.count = count;
        this.durationInSeconds = durationInSeconds;
        this.trainedAt = trainedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getTournamentId() {
        return tournamentId;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public Instant getTrainedAt() {
        return trainedAt;
    }
}
