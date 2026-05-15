package org.sports.exercise.battle.application.services;

import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.responses.UserStatsResponse;
import org.sports.exercise.battle.application.ports.PushUpRecordRepository;
import org.sports.exercise.battle.core.entities.PushUpRecord;
import org.sports.exercise.battle.core.entities.User;

public class StatsService {
    private final PushUpRecordRepository pushUpRecordRepository;
    private final UserRepository userRepository;

    public StatsService(UserRepository userRepository, PushUpRecordRepository pushUpRecordRepository){
        this.pushUpRecordRepository = pushUpRecordRepository;
        this.userRepository = userRepository;
    }

    public UserStatsResponse getUserStats(String authenticatedName){
        User user = userRepository.findByUsernameOrThrowNotFound(authenticatedName);

        int totalPushUps = pushUpRecordRepository.findByUserId(user.getId())
                .stream()
                .mapToInt(PushUpRecord::getCount)
                .sum();

        return new UserStatsResponse(user.getElo(), totalPushUps);
    }
}
