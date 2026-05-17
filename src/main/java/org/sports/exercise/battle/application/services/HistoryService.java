package org.sports.exercise.battle.application.services;

import org.sports.exercise.battle.application.responses.PushUpHistoryResponse;
import org.sports.exercise.battle.application.responses.dtos.PushUpHistoryEntry;
import org.sports.exercise.battle.application.responses.dtos.PushUpRecordDTO;
import org.sports.exercise.battle.application.ports.PushUpRecordRepository;
import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.requests.AddPushUpRecordRequest;
import org.sports.exercise.battle.core.entities.PushUpRecord;
import org.sports.exercise.battle.core.entities.Tournament;
import org.sports.exercise.battle.core.entities.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class HistoryService {
    private final UserRepository userRepository;
    private final PushUpRecordRepository pushUpRecordRepository;
    private static final Logger logger = Logger.getLogger(HistoryService.class.getName());

    public HistoryService(UserRepository userRepository, PushUpRecordRepository pushUpRecordRepository){
        this.userRepository = userRepository;
        this.pushUpRecordRepository = pushUpRecordRepository;
    }

    public PushUpRecordDTO addPushUpRecord(AddPushUpRecordRequest request, String authenticatedName, Tournament tournament){
        User user = userRepository.findByUsernameOrThrowNotFound(authenticatedName);

        PushUpRecord pushUpRecord = new PushUpRecord(
                UUID.randomUUID(),
                user.getId(),
                tournament.getId(),
                request.name(),
                request.count(),
                request.durationInSeconds(),
                Instant.now()
        );

        logger.info("User " + user.getUsername()
                + " added " + request.count()
                + " push-ups to tournament " + tournament.getId());

        pushUpRecordRepository.save(pushUpRecord);

        return new PushUpRecordDTO(pushUpRecord.getName(), pushUpRecord.getCount(), pushUpRecord.getDurationInSeconds());
    }

    public PushUpHistoryResponse getUserPushUpHistory(String authenticatedName){
        User user = userRepository.findByUsernameOrThrowNotFound(authenticatedName);

        List<PushUpRecord> pushUpRecords = pushUpRecordRepository.findByUserId(user.getId());

        List<PushUpHistoryEntry> pushUpHistoryEntries = pushUpRecords.stream()
                .map(pushUpRecord -> new PushUpHistoryEntry(
                        pushUpRecord.getTrainedAt().toString(),
                        pushUpRecord.getCount(),
                        pushUpRecord.getDurationInSeconds()
                )).toList();

        return new PushUpHistoryResponse(pushUpHistoryEntries);
    }
}
