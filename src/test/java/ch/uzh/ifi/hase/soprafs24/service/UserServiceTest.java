package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setStatus(UserStatus.OFFLINE);

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        // then

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateName_throwsException() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void getUser_validId_success() {
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        User foundUser = userService.getUser(testUser.getId());
        assertEquals(testUser.getId(), foundUser.getId());
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        assertEquals(UserStatus.OFFLINE, foundUser.getStatus());
    }

    @Test
    public void getUser_nonexistentUser_throwsException() {
        assertThrows(ResponseStatusException.class, () -> userService.getUser(testUser.getId()));
    }

    @Test
    public void putUser_validInput_success() {
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        User putUser = testUser;
        putUser.setUsername("test");
        userService.putUser(testUser.getId(), putUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        // Assertions
        assertEquals(testUser.getId(), putUser.getId());
        assertEquals(testUser.getUsername(), putUser.getUsername());
        assertEquals(UserStatus.OFFLINE, putUser.getStatus());
    }

    @Test
    public void putUser_nonexistentUser_throwsException() {
        assertThrows(ResponseStatusException.class, () -> userService.putUser(testUser.getId(), null));
    }


}