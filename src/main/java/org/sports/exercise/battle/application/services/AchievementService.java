package org.sports.exercise.battle.application.services;

import org.sports.exercise.battle.application.ports.PushUpRecordRepository;
import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.responses.AchievementResponse;
import org.sports.exercise.battle.application.responses.dtos.AchievementEntry;
import org.sports.exercise.battle.core.entities.PushUpRecord;
import org.sports.exercise.battle.core.entities.User;

import java.util.ArrayList;
import java.util.List;

public class AchievementService {
    private final PushUpRecordRepository pushUpRecordRepository;
    private final UserRepository userRepository;
    private static final int GETTING_STRONGER_ACHIEVEMENT = 100;
    private static final int BEAST_MODE_ACHIEVEMENT = 200;

    private static final int NEWBIE_ELO  = 150;
    private static final int INTERMEDIATE_ELO  = 200;
    private static final int ELITE_ATHLETE_ELO  = 300;


    public AchievementService(PushUpRecordRepository pushUpRecordRepository, UserRepository userRepository) {
        this.pushUpRecordRepository = pushUpRecordRepository;
        this.userRepository = userRepository;
    }

    public AchievementResponse getAchievements(String authenticatedName){
        User user = userRepository.findByUsernameOrThrowNotFound(authenticatedName);

        List<PushUpRecord> pushUpRecords = pushUpRecordRepository.findByUserId(user.getId());


        int totalPushUps = pushUpRecords.stream()
                .mapToInt(PushUpRecord::getCount)
                .sum();

        List<AchievementEntry> achievements = new ArrayList<>();

        if (!pushUpRecords.isEmpty()) {
            achievements.add(new AchievementEntry(
                    "Getting Started",
                    "Added your first push-up record"
            ));
        }

        if (totalPushUps >= GETTING_STRONGER_ACHIEVEMENT) {
            achievements.add(new AchievementEntry(
                    "Getting Stronger",
                    "Reached a total of" + GETTING_STRONGER_ACHIEVEMENT +  "push ups"
            ));
        }

        if (totalPushUps >= BEAST_MODE_ACHIEVEMENT) {
            achievements.add(new AchievementEntry(
                    "Beast Mode",
                    "Reached a total of " + BEAST_MODE_ACHIEVEMENT + " push-ups"
            ));
        }

        if (user.getElo() >= NEWBIE_ELO) {
            achievements.add(new AchievementEntry(
                    "Newbie Competitor",
                    "Reached " + NEWBIE_ELO + " ELO"
            ));
        }

        if (user.getElo() >= INTERMEDIATE_ELO) {
            achievements.add(new AchievementEntry(
                    "Intermediate Athlete",
                    "Reached " + INTERMEDIATE_ELO + " ELO"
            ));
        }

        if (user.getElo() >= ELITE_ATHLETE_ELO) {
            achievements.add(new AchievementEntry(
                    "Elite Athlete",
                    "Reached " + ELITE_ATHLETE_ELO + " ELO"
            ));
        }

        return new AchievementResponse(achievements);
    }
}
