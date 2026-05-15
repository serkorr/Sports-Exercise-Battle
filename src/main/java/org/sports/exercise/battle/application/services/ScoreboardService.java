package org.sports.exercise.battle.application.services;

import org.sports.exercise.battle.application.ports.PushUpRecordRepository;
import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.responses.ScoreboardResponse;
import org.sports.exercise.battle.application.responses.dtos.UserRanking;
import org.sports.exercise.battle.core.entities.PushUpRecord;
import org.sports.exercise.battle.core.entities.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreboardService {
    private final UserRepository userRepository;
    private final PushUpRecordRepository pushUpRecordRepository;

    public ScoreboardService(UserRepository userRepository, PushUpRecordRepository pushUpRecordRepository){
        this.userRepository = userRepository;
        this.pushUpRecordRepository = pushUpRecordRepository;
    }
    public ScoreboardResponse getScoreboard(String authenticatedUsername){
        userRepository.findByUsernameOrThrowNotFound(authenticatedUsername);

        List<User> users = userRepository.findAll();

        if(users.isEmpty()){
            return new ScoreboardResponse(List.of());
        }

        List<UserRanking> userRankings = users.stream()
                .map(user -> {
                    int totalPushups = pushUpRecordRepository.findByUserId(user.getId()).stream().mapToInt(PushUpRecord::getCount).sum();

                    //rank them after we have collected all user stats
                    return new UserRanking(0, user.getUsername(), user.getElo(), totalPushups);
                    //sort descending order
                }).sorted(Comparator.comparingInt(UserRanking::elo).reversed().thenComparing(Comparator.comparingInt(UserRanking::totalPushUps).reversed()))
                .toList();

        //rank the users now
        List<UserRanking> placements = new ArrayList<>();
        for(int i = 0; i < userRankings.size(); ++i){
            UserRanking ranking = userRankings.get(i);

            placements.add(new UserRanking(i, ranking.username(), ranking.elo(), ranking.totalPushUps()));
        }

        return new ScoreboardResponse(placements);
    }
}
