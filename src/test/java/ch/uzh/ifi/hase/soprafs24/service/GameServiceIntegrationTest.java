package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.repository.GameLobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("gamelobbyRepository")
    @Autowired
    private GameLobbyRepository gameLobbyRepository;

    @Qualifier("guestRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameLobbyService gameLobbyService;

    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
        gameLobbyRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    public void getGame_success() {
        assertEquals(gameRepository.findById(1L), Optional.empty());
        assertThrows(ResponseStatusException.class, () -> gameService.getGame(1L));

        Game testGame = new Game();
        testGame.setId(1L);

        gameRepository.save(testGame);
        gameRepository.flush();

        assertNotEquals(gameService.getGame(1L), Optional.empty());
    }
    @Test
    public void deleteGame_fail() {
        assertEquals(gameRepository.findById(1L), Optional.empty());
        assertThrows(ResponseStatusException.class, () -> gameService.deleteGame(1L));
    }

    @Test
    public void doRound_success() {
        assertEquals(gameRepository.findById(1L), Optional.empty());

        Game testGame = new Game();
        testGame.setId(1L);
        testGame.setGamepin(123456);
        testGame.setCurrentCard(0);
        gameRepository.save(testGame);
        gameRepository.flush();
        gameService.doRound(testGame);

        assertEquals(99, testGame.getCardStack().size());
    }

    @Test
    public void doRound_fail() {
        assertEquals(gameRepository.findById(1L), Optional.empty());
        Game testGame = new Game();
        testGame.setId(1L);
        assertThrows(NullPointerException.class, () -> gameService.doRound(testGame));
    }

    @Test
    public void updateGamestatus_fail() {
        assertEquals(gameRepository.findById(1L), Optional.empty());
        assertThrows(ResponseStatusException.class, () -> gameService.updateGamestatus(1L, 4));
    }

}