import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sports.exercise.battle.application.ports.PushUpRecordRepository;
import org.sports.exercise.battle.application.ports.TournamentRepository;
import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.services.TournamentService;
import org.sports.exercise.battle.core.entities.PushUpRecord;
import org.sports.exercise.battle.core.entities.Tournament;
import org.sports.exercise.battle.core.entities.TournamentStatus;
import org.sports.exercise.battle.core.entities.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TournamentServiceTest {
    private TournamentRepository tournamentRepository;
    private PushUpRecordRepository pushUpRecordRepository;
    private UserRepository userRepository;
    private TournamentService tournamentService;

    @BeforeEach
    public void setUp() {
        tournamentRepository = mock(TournamentRepository.class);
        pushUpRecordRepository = mock(PushUpRecordRepository.class);
        userRepository = mock(UserRepository.class);

        tournamentService = new TournamentService(
                tournamentRepository,
                pushUpRecordRepository,
                userRepository
        );
    }

    @Test
    public void whenEvaluatingTournament_winnerGetsEloIncreasedAndLoserGetsEloDecreased(){
        UUID tournamentId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        UUID loserId = UUID.randomUUID();

        Tournament tournament = new Tournament(
                tournamentId,
                Instant.now().minusSeconds(130),
                Instant.now().minusSeconds(10),
                TournamentStatus.RUNNING
        );

        //base elo 100
        User winner = new User(winnerId, "winner", "hash");
        User loser = new User(loserId, "loser", "hash");

        List<PushUpRecord> records = List.of(
                new PushUpRecord(UUID.randomUUID(), winnerId, tournamentId, "PushUps", 67, 70, Instant.now()),
                new PushUpRecord(UUID.randomUUID(), loserId, tournamentId, "PushUps", 3, 60, Instant.now())
        );


        when(tournamentRepository.findRunningTournament())
                .thenReturn(Optional.of(tournament));

        when(pushUpRecordRepository.findByTournamentId(tournamentId)).thenReturn(records);

        when(userRepository.find(winnerId))
                .thenReturn(Optional.of(winner));

        when(userRepository.find(loserId))
                .thenReturn(Optional.of(loser));

        tournamentService.getOrCreateRunningTournament();


        assertEquals(102, winner.getElo());
        assertEquals(99, loser.getElo());
        assertEquals(TournamentStatus.FINISHED, tournament.getStatus());

        verify(userRepository).update(winner);
        verify(userRepository).update(loser);
        verify(tournamentRepository).update(tournament);
    }

    @Test
    public void whenEvaluatingTournament_duringDraw_shouldUpdateWinnerAndLoserEloByOne(){
        UUID tournamentId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        UUID loserId = UUID.randomUUID();

        Tournament tournament = new Tournament(
                tournamentId,
                Instant.now().minusSeconds(130),
                Instant.now().minusSeconds(10),
                TournamentStatus.RUNNING
        );

        //base elo 100
        User winner = new User(winnerId, "winner", "hash");
        User loser = new User(loserId, "loser", "hash");

        List<PushUpRecord> records = List.of(
                new PushUpRecord(UUID.randomUUID(), winnerId, tournamentId, "PushUps", 67, 70, Instant.now()),
                new PushUpRecord(UUID.randomUUID(), loserId, tournamentId, "PushUps", 67, 60, Instant.now())
        );

        when(tournamentRepository.findRunningTournament())
                .thenReturn(Optional.of(tournament));

        when(pushUpRecordRepository.findByTournamentId(tournamentId)).thenReturn(records);

        when(userRepository.find(winnerId))
                .thenReturn(Optional.of(winner));

        when(userRepository.find(loserId))
                .thenReturn(Optional.of(loser));

        tournamentService.getOrCreateRunningTournament();


        assertEquals(101, winner.getElo());
        assertEquals(101, loser.getElo());
        assertEquals(TournamentStatus.FINISHED, tournament.getStatus());

        verify(userRepository).update(winner);
        verify(userRepository).update(loser);
        verify(tournamentRepository).update(tournament);
    }
}
