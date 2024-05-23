package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private UserService userService;

    private Player testPlayer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setName("testName");
        testPlayer.setShame_tokens(0);
        testPlayer.setIsUser(null);

        // when -> any object is being save in the playerRepository -> return the dummy
        // testPlayer
        Mockito.when(playerRepository.save(Mockito.any())).thenReturn(testPlayer);
    }

    @Test
    public void createPlayer_validInputs_success() {
        // when -> any object is being save in the playerRepository -> return the dummy
        // testPlayer
        Player createdPlayer = playerService.createPlayer(testPlayer);
        Mockito.verify(playerRepository, Mockito.times(1)).save(Mockito.any());

        // then

        assertEquals(testPlayer.getId(), createdPlayer.getId());
        assertEquals(testPlayer.getName(), createdPlayer.getName());
        assertNotNull(createdPlayer.getToken());
    }

    @Test
    public void getPlayer_validId_success() {
        Mockito.when(playerRepository.findById(testPlayer.getId())).thenReturn(Optional.of(testPlayer));
        Player foundPlayer = playerService.getPlayer(testPlayer.getId());
        assertEquals(testPlayer.getId(), foundPlayer.getId());
        assertEquals(testPlayer.getName(), foundPlayer.getName());
    }

    @Test
    public void getPlayer_nonexistentPlayer_throwsException() {
        assertThrows(ResponseStatusException.class, () -> playerService.getPlayer(testPlayer.getId()));
    }

    @Test
    public void addShame_token_success()    {
        Mockito.when(playerRepository.findById(testPlayer.getId())).thenReturn(Optional.of(testPlayer));
        playerService.addShame_token(testPlayer.getId());
        assertEquals(testPlayer.getId(), testPlayer.getId());
        assertEquals(testPlayer.getName(), testPlayer.getName());
        assertEquals(1, testPlayer.getShame_tokens());
    }

    @Test
    public void addShame_token_fail_nonExistentPlayer() {

        Mockito.when(playerRepository.findById(testPlayer.getId()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found."));
        assertThrows(ResponseStatusException.class, () -> playerService.addShame_token(testPlayer.getId()));
    }

    @Test
    public void loginUser_success() {
        User user = new User();
        user.setUsername("testName");
        user.setPassword("testPassword");
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        Player player = playerService.loginUser(user);
        assertEquals(testPlayer.getName(), player.getName());
    }

    @Test
    public void loginUser_fail_userNotFound()   {

        User user = new User();
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found."));
        assertThrows(ResponseStatusException.class, () -> playerService.loginUser(user));
    }
    @Test
    public void loginUser_fail_wrongPassword() {
        User user = new User();
        user.setUsername("testName");
        user.setPassword("wrongPassword");
        User storedUser = new User();
        storedUser.setUsername("testName");
        storedUser.setPassword("correctPassword");
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(storedUser);
        assertThrows(ResponseStatusException.class, () -> playerService.loginUser(user));
    }

    @Test
    public void logoutUser_success() {
        Mockito.when(playerRepository.findById(testPlayer.getId())).thenReturn(Optional.of(testPlayer));
        Player loggedOutPlayer = playerService.logoutUser(testPlayer.getId());
        Mockito.verify(playerRepository, Mockito.times(1)).delete(testPlayer);
        Mockito.verify(playerRepository, Mockito.times(1)).flush();
        assertEquals(testPlayer.getId(), loggedOutPlayer.getId());
    }

    @Test
    public void logoutUser_fail_nonExistentPlayer() {
        Mockito.when(playerRepository.findById(testPlayer.getId())).thenReturn(Optional.empty());
        Player loggedOutPlayer = playerService.logoutUser(testPlayer.getId());
        assertNull(loggedOutPlayer);
    }

    @Test
    public void generateNameAddition_uniqueName() {
        Mockito.when(playerRepository.findByName(Mockito.anyString())).thenReturn(null);
        String nameAddition = playerService.generateNameAddition();
        assertNotNull(nameAddition);
        assertEquals(4, nameAddition.length());
    }

    @Test
    public void getPlayers_success() {
        Player anotherPlayer = new Player();
        anotherPlayer.setId(2L);
        anotherPlayer.setName("anotherTestName");
        Mockito.when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer, anotherPlayer));
        assertEquals(2, playerService.getPlayers().size());
    }

    @Test
    public void increaseGamesPlayed_success() {
        User user = new User();
        user.setId(1L);
        testPlayer.setIsUser(user.getId());
        Mockito.when(userService.getUser(user.getId())).thenReturn(user);
        Player player = playerService.increaseGamesPlayed(testPlayer);
        Mockito.verify(userService, Mockito.times(1)).increaseGamesPlayed(user);
        assertEquals(testPlayer, player);
    }

    @Test
    public void increaseRoundsWon_success() {
        User user = new User();
        user.setId(1L);
        testPlayer.setIsUser(user.getId());
        Mockito.when(userService.getUser(user.getId())).thenReturn(user);
        Player player = playerService.increaseRoundsWon(testPlayer, 1);
        Mockito.verify(userService, Mockito.times(1)).increaseRoundsWon(user, 1);
        assertEquals(testPlayer, player);
    }

    @Test
    public void increaseFlawlessWin_success() {
        User user = new User();
        user.setId(1L);
        testPlayer.setIsUser(user.getId());
        Mockito.when(userService.getUser(user.getId())).thenReturn(user);
        Player player = playerService.increaseFlawlessWin(testPlayer);
        Mockito.verify(userService, Mockito.times(1)).increaseFlawlessWin(user);
        assertEquals(testPlayer, player);
    }

    @Test
    public void loginPlayer_success() {
        // Arrange
        Player savedPlayer = new Player();
        savedPlayer.setId(1L);
        savedPlayer.setName("Guest#1234");
        savedPlayer.setToken(UUID.randomUUID().toString());
        Mockito.when(playerRepository.save(Mockito.any(Player.class))).thenReturn(savedPlayer);

        // Act
        Player result = playerService.loginPlayer();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertTrue(result.getName().startsWith("Guest#"));
        Mockito.verify(playerRepository, Mockito.times(1)).save(Mockito.any(Player.class));
        Mockito.verify(playerRepository, Mockito.times(1)).flush();
    }

    @Test
    public void logoutUser_success2() {
        // Arrange
        Mockito.when(playerRepository.findById(testPlayer.getId())).thenReturn(Optional.of(testPlayer));

        // Act
        Player result = playerService.logoutUser(testPlayer.getId());

        // Assert
        assertNotNull(result);
        assertEquals(testPlayer.getId(), result.getId());
        Mockito.verify(playerRepository, Mockito.times(1)).delete(testPlayer);
        Mockito.verify(playerRepository, Mockito.times(1)).flush();
    }

    @Test
    public void logoutUser_nonExistentPlayer_returnsNull() {
        // Arrange
        Mockito.when(playerRepository.findById(testPlayer.getId())).thenReturn(Optional.empty());

        // Act
        Player result = playerService.logoutUser(testPlayer.getId());

        // Assert
        assertNull(result);
        Mockito.verify(playerRepository, Mockito.never()).delete(Mockito.any(Player.class));
        Mockito.verify(playerRepository, Mockito.never()).flush();
    }

    @Test
    public void logoutUser_throwsException() {
        // Arrange
        Mockito.when(playerRepository.findById(testPlayer.getId())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> playerService.logoutUser(testPlayer.getId()));
    }


}