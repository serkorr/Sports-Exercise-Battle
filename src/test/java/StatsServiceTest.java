import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sports.exercise.battle.application.ports.PushUpRecordRepository;
import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.responses.UserStatsResponse;
import org.sports.exercise.battle.application.services.StatsService;
import org.sports.exercise.battle.core.entities.PushUpRecord;
import org.sports.exercise.battle.core.entities.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class StatsServiceTest {
    private UserRepository userRepository;
    private PushUpRecordRepository pushUpRecordRepository;
    private StatsService statsService;

    @BeforeEach()
    public void setUp(){
        this.userRepository = mock(UserRepository.class);
        this.pushUpRecordRepository = mock(PushUpRecordRepository.class);
        this.statsService = new StatsService(userRepository, pushUpRecordRepository);
    }

    @Test
    public void whenRequestingStats_shouldReturnEloAndTotalPushUps(){
        User existingUser = new User(
                java.util.UUID.randomUUID(),
                "serkan",
                "someHash"
        );

        UUID tournamentId = UUID.randomUUID();

        List<PushUpRecord> records = new ArrayList<>();
        records.add(new PushUpRecord(
                UUID.randomUUID(),
                existingUser.getId(),
                tournamentId,
                "PushUps",
                40,
                60,
                Instant.now()
        ));
        records.add(new PushUpRecord(
                UUID.randomUUID(),
                existingUser.getId(),
                tournamentId,
                "PushUps",
                11,
                25,
                Instant.now()
        ));

        int expectedTotalPushUps = 51;

        existingUser.increaseElo(200);

        when(userRepository.findByUsernameOrThrowNotFound("serkan")).thenReturn(existingUser);
        when(pushUpRecordRepository.findByUserId(existingUser.getId())).thenReturn(records);

        UserStatsResponse response = statsService.getUserStats(existingUser.getUsername());

        assertNotNull(response);
        assertEquals(existingUser.getElo(), response.elo());
        assertEquals(expectedTotalPushUps, response.totalPushUps());

        verify(userRepository).findByUsernameOrThrowNotFound("serkan");
        verify(pushUpRecordRepository).findByUserId(existingUser.getId());

    }
}
