import org.junit.jupiter.api.Test;
import org.sports.exercise.battle.infrastructure.security.PasswordHasher;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {

    @Test
    void whenHashingPassword_shouldVerifyCorrectPassword(){
        String password = "sirkan";

        String hash = PasswordHasher.hashPassword(password.toCharArray());

        boolean verified = PasswordHasher.verifyPassword(password.toCharArray(), hash);

        assertTrue(verified);
    }

    @Test
    void whenHashingSamePassword_shouldCreateDifferentHashes(){
        String password = "sirkan";

        String hash1 = PasswordHasher.hashPassword(password.toCharArray());
        String hash2 = PasswordHasher.hashPassword(password.toCharArray());

        assertNotEquals(hash1, hash2);

    }

    @Test
    void whenVerifyingWrongPassword_shouldReturnFalse(){
        String correctPassword = "sirkan";
        String wrongPassoword = "nakris";
        String hash = PasswordHasher.hashPassword(correctPassword.toCharArray());
        boolean verified = PasswordHasher.verifyPassword(wrongPassoword.toCharArray(), hash);

        assertFalse(verified);
    }


}
