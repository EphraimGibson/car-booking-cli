package entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testShouldThrowErrorWhenCreateUserWithEmptyName() {
        assertInvalidUserName("");
    }

    @Test
    void testShouldThrowErrorWhenCreateUserWithNullName() {
        assertInvalidUserName(null);
    }

    @Test
    void testShouldThrowErrorWhenCreateUserWithBlankName() {
        assertInvalidUserName("    ");
    }

    private void assertInvalidUserName(String invalidName) {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new User(invalidName),
                "invalid name cannot be created");

        Assertions.assertEquals("User must have a name, please fill name field", exception.getMessage());
    }
}