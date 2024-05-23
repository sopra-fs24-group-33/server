package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import ch.uzh.ifi.hase.soprafs24.repository.GameLobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ResponseStatusException;

public class GameLobbyServiceTest {

    @Mock
    private GameLobbyRepository gamelobbyRepository;

    private GameLobbyService gamelobbyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        gamelobbyService = new GameLobbyService(gamelobbyRepository);
    }

    @Test
    public void createGameLobby_validInputs_success() {
        Player admin = new Player();
        admin.setId(1L);
        admin.setName("Admin");

        GameLobby savedGameLobby = new GameLobby();
        savedGameLobby.setPin(111111);
        when(gamelobbyRepository.save(any(GameLobby.class))).thenReturn(savedGameLobby);
        GameLobby createdGameLobby = gamelobbyService.createGameLobby(admin);
        assertNotNull(createdGameLobby);
        assertEquals(admin.getId(), createdGameLobby.getAdmin());
        assertNotNull(createdGameLobby.getPin());
        assertNotEquals(0, createdGameLobby.getPin());
        verify(gamelobbyRepository, times(1)).save(any(GameLobby.class));
    }

    @Test
    public void getGameLobby_existingPin_success() {
        int pin = 123456;
        GameLobby existingGameLobby = new GameLobby();
        existingGameLobby.setPin(pin);
        when(gamelobbyRepository.findByPin(pin)).thenReturn(existingGameLobby);

        GameLobby retrievedGameLobby = gamelobbyService.getGameLobby(pin);

        assertNotNull(retrievedGameLobby);
        assertEquals(pin, retrievedGameLobby.getPin());
    }

    @Test
    public void getGameLobby_nonExistingPin_throwException() {
        int nonExistingPin = 999999;
        when(gamelobbyRepository.findByPin(nonExistingPin)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> gamelobbyService.getGameLobby(nonExistingPin));
    }

    @Test
    public void addPlayer_validInputs_success() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");

        GameLobby lobby = new GameLobby();

        GameLobby updatedLobby = gamelobbyService.addPlayer(player, lobby);

        assertEquals(1, updatedLobby.getPlayers().size());
        assertEquals(player.getName(), updatedLobby.getPlayers().get(0).getName());
        assertEquals(player.getId(), updatedLobby.getPlayers().get(0).getId());
        verify(gamelobbyRepository, times(1)).save(any(GameLobby.class));
        verify(gamelobbyRepository, times(1)).flush();
    }
    @Test
    public void removePlayer_validInputs_success() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");

        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setId(1L);
        gamePlayer.setName("Player1");

        GameLobby lobby = new GameLobby();
        lobby.addPlayer(gamePlayer);

        GameLobby updatedLobby = gamelobbyService.removePlayer(player, lobby);

        assertTrue(updatedLobby.getPlayers().isEmpty());
        verify(gamelobbyRepository, times(1)).save(any(GameLobby.class));
        verify(gamelobbyRepository, times(1)).flush();
    }

    @Test
    public void deleteReference_validPin_success() {
        int gamePin = 123456;

        GameLobby lobby = new GameLobby();
        lobby.setPin(gamePin);

        when(gamelobbyRepository.findByPin(gamePin)).thenReturn(lobby);

        gamelobbyService.deleteReference(gamePin);

        assertNull(lobby.getGameid());
        verify(gamelobbyRepository, times(1)).findByPin(gamePin);
        verify(gamelobbyRepository, times(1)).save(lobby);
        verify(gamelobbyRepository, times(1)).flush();
    }

    @Test
    public void deleteReference_invalidPin_throwException() {
        int gamePin = 123456;

        when(gamelobbyRepository.findByPin(gamePin)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> gamelobbyService.deleteReference(gamePin));
        verify(gamelobbyRepository, never()).save(any(GameLobby.class));
        verify(gamelobbyRepository, never()).flush();
    }

    @Test
    public void deleteReference_success() {
        int gamePin = 111111;
        GameLobby gameLobby = new GameLobby();
        gameLobby.setPin(gamePin);
        gameLobby.setGameid(1L);
        Mockito.when(gamelobbyRepository.findByPin(gamePin)).thenReturn(gameLobby);

        gamelobbyService.deleteReference(gamePin);

        assertNull(gameLobby.getGameid());
        verify(gamelobbyRepository, times(1)).save(gameLobby);
        verify(gamelobbyRepository, times(1)).flush();
    }

    @Test
    public void deleteReference_gameLobbyNotFound_throwsException() {
        int gamePin = 111111;
        Mockito.when(gamelobbyRepository.findByPin(gamePin)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> gamelobbyService.deleteReference(gamePin));
    }

    @Test
    public void getGameLobby_success() {
        int gamePin = 111111;
        GameLobby gameLobby = new GameLobby();
        gameLobby.setPin(gamePin);
        Mockito.when(gamelobbyRepository.findByPin(gamePin)).thenReturn(gameLobby);

        GameLobby result = gamelobbyService.getGameLobby(gamePin);

        assertNotNull(result);
        assertEquals(gamePin, result.getPin());
    }

    @Test
    public void getGameLobby_gameLobbyNotFound_throwsException() {
        int gamePin = 111111;
        Mockito.when(gamelobbyRepository.findByPin(gamePin)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> gamelobbyService.getGameLobby(gamePin));
    }

    @Test
    public void addGameId_success() {
        int gamePin = 111111;
        Long gameId = 1L;
        GameLobby gameLobby = new GameLobby();
        gameLobby.setPin(gamePin);
        Mockito.when(gamelobbyRepository.findByPin(gamePin)).thenReturn(gameLobby);

        gamelobbyService.addGameId(gamePin, gameId);

        assertEquals(gameId, gameLobby.getGameid());
        verify(gamelobbyRepository, times(1)).save(gameLobby);
        verify(gamelobbyRepository, times(1)).flush();
    }

    @Test
    public void addGameId_gameLobbyNotFound_throwsException() {
        int gamePin = 111111;
        Long gameId = 1L;
        Mockito.when(gamelobbyRepository.findByPin(gamePin)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> gamelobbyService.addGameId(gamePin, gameId));
    }
}
