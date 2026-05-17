import org.junit.jupiter.api.Test;
import org.sports.exercise.battle.server.auth.AuthService;
import org.sports.exercise.battle.server.exceptions.UnauthorizedException;
import org.sports.exercise.battle.server.messages.HttpRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthServiceTest {

    @Test
    public void shouldExtractUsernameFromToken(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic kienboc-sebToken");
        HttpRequest httpRequest = new HttpRequest("GET", "/users/kienboc", headers, "");
        String actualUsername = "kienboc";

        AuthService authService = new AuthService();

        String extractedUsername = authService.getAuthenticatedUsername(httpRequest);

        assertEquals(actualUsername, extractedUsername);

    }

    @Test
    public void ShouldThrowExceptionWhenAuthorizationHeaderIsInvalid(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Verification", "Basic kienboc-sebToken");

        HttpRequest httpRequest = new HttpRequest("GET", "/users/kienboc", headers, "");

        AuthService authService = new AuthService();

        assertThrows(UnauthorizedException.class, () -> authService.getAuthenticatedUsername(httpRequest));

    }

    @Test
    public void ShouldThrowExceptionWhenTokenIsInvalid(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic kienboc-XYZ");

        HttpRequest httpRequest = new HttpRequest("GET", "/users/kienboc", headers, "");

        AuthService authService = new AuthService();

        assertThrows(UnauthorizedException.class, () -> authService.getAuthenticatedUsername(httpRequest));
    }

    @Test
    public void ShouldThrowExceptionWhenAuthorizationHeaderIsMissing(){
        Map<String, String> headers = new HashMap<>();

        HttpRequest httpRequest = new HttpRequest("GET", "/users/kienboc", headers, "");

        AuthService authService = new AuthService();

        assertThrows(UnauthorizedException.class, () -> authService.getAuthenticatedUsername(httpRequest));
    }
}
