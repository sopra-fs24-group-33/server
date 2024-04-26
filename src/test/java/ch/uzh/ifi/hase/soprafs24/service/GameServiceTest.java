package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameLobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameLobbyRepository gamelobbyRepository;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private GameService gameService;

    private Game testGame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        gameService = new GameService(gameRepository, playerService);
        testGame = new Game();
        testGame.setId(1L);
        testGame.setGamepin(111111);
        testGame.setLevel(1);
    }

    @Test
    public void startGame_validInputs_success() {
        // Given
        GameLobby testLobby = new GameLobby();
        testLobby.setPin(111111);

        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("Player1");

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Player2");

        Set<GamePlayer> players = new HashSet<>();
        GamePlayer gamePlayer1 = new GamePlayer();
        gamePlayer1.setId(player1.getId());
        gamePlayer1.setName(player1.getName());
        players.add(gamePlayer1);

        GamePlayer gamePlayer2 = new GamePlayer();
        gamePlayer2.setId(player2.getId());
        gamePlayer2.setName(player2.getName());
        players.add(gamePlayer2);

        testLobby.setGamePlayers(players);
        testLobby.setAdmin(player1.getId());
        testGame.setPlayers(players);

        // Mock behavior of the repository
        Mockito.when(gamelobbyRepository.save(Mockito.any())).thenReturn(testLobby);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);

        // Mock behavior of the player service
        Mockito.doNothing().when(playerService).addShame_token(Mockito.anyLong());

        // When
        Game createdGame = gameService.startGame(testLobby);

        // Then
        assertNotNull(createdGame);
        assertEquals(111111, createdGame.getGamepin());
        assertEquals(players, createdGame.getPlayers());
        assertEquals(1, createdGame.getLevel());
        assertEquals(0, createdGame.getSuccessfulMove());
        assertEquals(0, createdGame.getCurrentCard());
        // Additional assertions as needed
    }
}
