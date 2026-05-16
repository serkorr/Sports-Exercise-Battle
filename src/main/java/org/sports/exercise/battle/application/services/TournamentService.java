package org.sports.exercise.battle.application.services;

import org.sports.exercise.battle.application.ports.TournamentRepository;
import org.sports.exercise.battle.core.entities.Tournament;
import org.sports.exercise.battle.core.entities.TournamentStatus;

import java.time.Instant;
import java.util.UUID;

public class TournamentService {
    private final TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository){
        this.tournamentRepository = tournamentRepository;
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
        }

        Instant now = Instant.now();

        Tournament newTournament = new Tournament(
                UUID.randomUUID(),
                now,
                now.plusSeconds(120), // 2 minute timeframe
                TournamentStatus.RUNNING
        );

        tournamentRepository.save(newTournament);

        return newTournament;

    }
}
