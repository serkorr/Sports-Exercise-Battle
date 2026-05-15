package org.sports.exercise.battle.application.services;

import org.sports.exercise.battle.application.responses.PushUpHistoryResponse;
import org.sports.exercise.battle.application.responses.dtos.PushUpHistoryEntry;
import org.sports.exercise.battle.application.responses.dtos.PushUpRecordDTO;
import org.sports.exercise.battle.application.ports.PushUpRecordRepository;
import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.requests.AddPushUpRecordRequest;
import org.sports.exercise.battle.core.entities.PushUpRecord;
import org.sports.exercise.battle.core.entities.User;

import java.util.List;

public class HistoryService {
    private final UserRepository userRepository;
    private final PushUpRecordRepository pushUpRecordRepository;

    public HistoryService(UserRepository userRepository, PushUpRecordRepository pushUpRecordRepository){
        this.userRepository = userRepository;
        this.pushUpRecordRepository = pushUpRecordRepository;
    }

    public PushUpRecordDTO addPushUpRecord(AddPushUpRecordRequest request, String authenticatedName){
        User user = userRepository.findByUsernameOrThrowNotFound(authenticatedName);
        return null;//placeholder
    }

    public PushUpHistoryResponse getUserPushUpHistory(String authenticatedName){
        User user = userRepository.findByUsernameOrThrowNotFound(authenticatedName);

        //PushUpHistory response consists of PushupHistory entry containing date, count, duration in seconds

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
