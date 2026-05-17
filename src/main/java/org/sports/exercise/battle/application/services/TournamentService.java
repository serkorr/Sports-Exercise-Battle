package org.sports.exercise.battle.application.services;

import org.sports.exercise.battle.application.ports.PushUpRecordRepository;
import org.sports.exercise.battle.application.ports.TournamentRepository;
import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.responses.TournamentResponse;
import org.sports.exercise.battle.application.responses.dtos.TournamentParticipant;
import org.sports.exercise.battle.core.entities.PushUpRecord;
import org.sports.exercise.battle.core.entities.Tournament;
import org.sports.exercise.battle.core.entities.TournamentStatus;
import org.sports.exercise.battle.core.entities.User;
import org.sports.exercise.battle.core.exceptions.UserNotFoundException;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final PushUpRecordRepository pushUpRecordRepository;
    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(TournamentService.class.getName());

    public TournamentService(TournamentRepository tournamentRepository, PushUpRecordRepository pushUpRecordRepository, UserRepository userRepository){
        this.tournamentRepository = tournamentRepository;
        this.pushUpRecordRepository = pushUpRecordRepository;
        this.userRepository = userRepository;
    }

    public Tournament getOrCreateRunningTournament(){
        var optionalTournament = tournamentRepository.findRunningTournament();

        if(optionalTournament.isPresent()){
            Tournament runningTournament = optionalTournament.get();

            //tournament is still running
            if(Instant.now().isBefore(runningTournament.getEndedAt())){
                return runningTournament;
            }

            //tournament ended
            //evaluate tournament
            evaluateTournament(runningTournament);
        }

        Instant now = Instant.now();

        Tournament newTournament = new Tournament(
                UUID.randomUUID(),
                now,
                now.plusSeconds(120), // 2 minute timeframe
                TournamentStatus.RUNNING
        );

        logger.info("Tournament started: " + newTournament.getId()
                + " from " + newTournament.getStartedAt()
                + " to " + newTournament.getEndedAt());

        tournamentRepository.save(newTournament);

        return newTournament;

    }

    public TournamentResponse getTournamentInfo(String authenticatedName){
        userRepository.findByUsernameOrThrowNotFound(authenticatedName);

        var optionalTournament = tournamentRepository.findRunningTournament();

        if(optionalTournament.isPresent()){
            Tournament tournament = optionalTournament.get();

            if(Instant.now().isAfter(tournament.getEndedAt())){
                evaluateTournament(tournament);
            }

            return createTournamentResponse(tournament, "FINISHED");
        }

        Optional<Tournament> optionalLatestTournament = tournamentRepository.findLatestTournament();

        if(optionalLatestTournament.isPresent()){
            Tournament latest = optionalLatestTournament.get();

            return createTournamentResponse(latest, latest.getStatus().name());
        }

        return new TournamentResponse(
                "",
                "NO_ACTIVE_TOURNAMENT",
                "",
                "",
                List.of()
        );
    }

    private TournamentResponse createTournamentResponse(Tournament tournament, String status){
        List<PushUpRecord> pushUpRecords = pushUpRecordRepository.findByTournamentId(tournament.getId());

        Map<UUID, Integer> totalPushUpsByUser = getTotalPushUpsByUser(pushUpRecords);

        List<TournamentParticipant> participants= totalPushUpsByUser.entrySet().stream().map(
                entry -> {
                    User user = userRepository.find(entry.getKey()).orElseThrow(() -> new UserNotFoundException(entry.getKey().toString()));

                    return new TournamentParticipant(user.getUsername(), entry.getValue());
                })
                .toList();

        return new TournamentResponse(tournament.getId().toString(), status, tournament.getStartedAt().toString(), tournament.getEndedAt().toString(), participants);
    }

    private Map<UUID, Integer> getTotalPushUpsByUser(List<PushUpRecord> pushUpRecords) {
        Map<UUID, Integer> totalPushUpsByUser = new HashMap<>();

        for(PushUpRecord record : pushUpRecords){
            int count = record.getCount();
            UUID userId = record.getUserId();

            int currentTotal = totalPushUpsByUser.getOrDefault(userId, 0 );

            totalPushUpsByUser.put(userId, currentTotal + count);
        }
        return totalPushUpsByUser;
    }

    private void evaluateTournament(Tournament tournament){
        List<PushUpRecord> pushUpRecords = pushUpRecordRepository.findByTournamentId(tournament.getId());

        if(pushUpRecords.isEmpty()){
            tournament.finish();
            tournamentRepository.update(tournament);
            return;
        }

        //create dictionary for player id and their total pushups
        Map<UUID, Integer> totalPushUpsByUser = getTotalPushUpsByUser(pushUpRecords);

        //get highest pushups
        int highestPushUps = totalPushUpsByUser.values()
                .stream()
                .max(Comparator.naturalOrder())
                .orElseThrow();

        //get winner ids with the highest pushups
        List<UUID> winnerIds = totalPushUpsByUser.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == highestPushUps)
                .map(Map.Entry::getKey)
                .toList();

        //check if draw
        boolean isDraw = winnerIds.size() > 1;

        logger.info("Evaluating tournament: " + tournament.getId());
        logger.info("Winner IDs: " + winnerIds + ", draw: " + isDraw);

        //increase/decrease elo of all players according to the match
        for(UUID userId : totalPushUpsByUser.keySet()){
            User user = userRepository.find(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));

            if(winnerIds.contains(user.getId())){
                if(isDraw){
                    user.increaseElo(1);
                    logger.info("Ended up in a draw, user (" + user.getUsername() + ") got their ELO increased by 1" );
                } else{
                    user.increaseElo(2);
                    logger.info("Ended up in a win, user (" + user.getUsername() + ") got their ELO increased by 2" );
                }
            }else{
                user.decreaseElo(1);
                logger.info("Ended up in a loss, user (" + user.getUsername() + ") got their ELO decreased by 1" );
            }
            //update in user repo
            userRepository.update(user);
        }

        //finish the tournament
        tournament.finish();

        //update the tournament
        tournamentRepository.update(tournament);
    }
}
