package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
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

public class GameLobbyServiceTest {

    @Mock
    private GameLobbyRepository gamelobbyRepository;

    @InjectMocks
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
