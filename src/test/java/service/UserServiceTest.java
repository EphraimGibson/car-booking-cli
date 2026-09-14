package service;

import entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.IPersistence;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IPersistence persistenceMock;
    @InjectMocks
    private UserService userService;

    @Test
    void testGetAllUsersSuccessful() {
        //Given
        User testUser = new User("John");
        when(persistenceMock.getAllUsers()).thenReturn(List.of(testUser));

        //When
        List<User> result = userService.getAllUsers();

        //Then
        Assertions.assertNotNull(result);

        boolean userExists = false;
        for (User user : result) {
            if (Objects.equals(user.getId(), testUser.getId())) {
                userExists = true;
                break;
            }
        }
        Assertions.assertTrue(userExists, "User" + testUser.getId() + "does not exist in the returned users");
        Assertions.assertEquals(1, result.size(), "user results should have only one user");
        verify(persistenceMock, times(1)).getAllUsers();
    }

}