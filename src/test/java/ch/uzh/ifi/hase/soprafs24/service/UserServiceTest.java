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
import org.springframework.http.HttpStatus;
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

    @Test
    public void addShame_token_success() {
        // Arrange
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        // Act
        userService.addShame_token(testUser.getId());

        // Assert
        assertEquals(1, testUser.getShame_tokens());
        Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).flush();
    }

    @Test
    public void addShame_token_userNotFound_throwsException() {
        // Arrange
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.addShame_token(testUser.getId()));
    }

    @Test
    public void addShame_token_exceptionThrown() {
        // Arrange
        Mockito.when(userRepository.findById(testUser.getId())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.addShame_token(testUser.getId()));
    }

    @Test
    public void logoutUser_success() {
        // Arrange
        testUser.setStatus(UserStatus.ONLINE);
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        User result = userService.logoutUser(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(UserStatus.OFFLINE, result.getStatus());
        Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).flush();
    }

    @Test
    public void logoutUser_userNotFound_throwsException() {
        // Arrange
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userService.logoutUser(testUser));
    }
    @Test
    public void increaseGamesPlayed_success() {
        // Arrange
        testUser.setGamesPlayed(1);
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        User result = userService.increaseGamesPlayed(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getGamesPlayed());
        Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).flush();
    }

    @Test
    public void increaseRoundsWon_success() {
        // Arrange
        testUser.setRoundsWon(2);
        int level = 3;
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        User result = userService.increaseRoundsWon(testUser, level);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getRoundsWon()); // 2 + (3 - 1)
        Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).flush();
    }

    @Test
    public void increaseFlawlessWin_success() {
        // Arrange
        testUser.setFlawlessWins(1);
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);

        // Act
        User result = userService.increaseFlawlessWin(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getFlawlessWins());
        Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).flush();
    }




}