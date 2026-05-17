import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sports.exercise.battle.application.common.UserDTOConverter;
import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.requests.LogInUserRequest;
import org.sports.exercise.battle.application.requests.RegisterUserRequest;
import org.sports.exercise.battle.application.responses.LoginResponse;
import org.sports.exercise.battle.application.responses.dtos.UserDTO;
import org.sports.exercise.battle.application.services.UserService;
import org.sports.exercise.battle.core.entities.User;
import org.sports.exercise.battle.core.exceptions.InvalidCredentialsException;
import org.sports.exercise.battle.core.exceptions.UserAlreadyExistsException;
import org.sports.exercise.battle.infrastructure.security.PasswordHasher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        this.userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository, new UserDTOConverter());
    }

    @Test
    public void whenRegisteringUser_shouldSaveInUserRepository(){
        RegisterUserRequest request = new RegisterUserRequest("serkan", "1234");
        int expectedElo = 100;
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());

        UserDTO result = userService.register(request);

        assertEquals(request.username(), result.username());
        assertEquals(expectedElo, result.elo());

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void whenRegisteringExistingUser_shouldRejectDuplicateUsername(){
        User existingUser = new User(
                java.util.UUID.randomUUID(),
                "serkan",
                "someHash"
        );
        RegisterUserRequest request = new RegisterUserRequest("serkan", "1234");


        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistsException.class, ()-> userService.register(request));

        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    public void whenLoginWithCorrectPassword_shouldReturnValidToken(){
        String actualPassword = "1234";
        String actualUsername = "serkan";
        String tokenSuffix = "-sebToken";
        String passwordHash = PasswordHasher.hashPassword(actualPassword.toCharArray());
        User existingUser = new User(
                java.util.UUID.randomUUID(),
                actualUsername,
                passwordHash
        );

        when(userRepository.findByUsernameOrThrowNotFound(actualUsername)).thenReturn(existingUser);

        LogInUserRequest logInUserRequest = new LogInUserRequest("serkan", "1234");

        LoginResponse response = userService.login(logInUserRequest);

        assertNotNull(response);
        assertNotNull(response.token());
        assertTrue(response.token().endsWith(tokenSuffix));
        assertTrue(response.token().startsWith(actualUsername));
        assertEquals("serkan-sebToken", response.token());

        verify(userRepository).findByUsernameOrThrowNotFound(actualUsername);
    }

    @Test
    public void whenLoginWithWrongPassword_shouldThrowException(){
        String actualPassword = "1234";
        String actualUsername = "serkan";
        String passwordHash = PasswordHasher.hashPassword(actualPassword.toCharArray());
        User existingUser = new User(
                java.util.UUID.randomUUID(),
                actualUsername,
                passwordHash
        );

        when(userRepository.findByUsernameOrThrowNotFound(actualUsername)).thenReturn(existingUser);

        LogInUserRequest logInUserRequest = new LogInUserRequest("serkan", "wrong_password");

        assertThrows(InvalidCredentialsException.class, () -> userService.login(logInUserRequest));

        verify(userRepository).findByUsernameOrThrowNotFound(actualUsername);
    }
}
