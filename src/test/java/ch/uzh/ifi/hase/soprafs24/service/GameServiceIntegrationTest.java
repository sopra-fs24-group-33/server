package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.repository.GameLobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GamePlayerRepository;
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

    @Qualifier("gamePlayerRepository")
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

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
    public void getGame_fail() {
        assertEquals(gameRepository.findById(123456L), Optional.empty());
    }

    @Test
    public void deleteGame_success() {
        assertEquals(gameRepository.findById(1L), Optional.empty());

        Game testGame = new Game();
        testGame.setId(1L);

        gameRepository.save(testGame);
        gameRepository.flush();

        assertNotEquals(gameService.getGame(1L), Optional.empty());

        gameService.deleteGame(1L);

        assertEquals(gameRepository.findById(1L), Optional.empty());
    }

    @Test
    public void deleteGame_fail() {
        assertEquals(gameRepository.findById(1L), Optional.empty());
        assertThrows(ResponseStatusException.class, () -> gameService.deleteGame(1L));
    }

    @Test
    public void startGame_success() {
        assertEquals(gameRepository.findById(1L), Optional.empty());
        assertEquals(gameLobbyRepository.findById(222), Optional.empty());

        Game testGame = new Game();
        testGame.setId(1L);
        testGame.setGamepin(123456);
        gameRepository.save(testGame);
        gameRepository.flush();
        Player testPlayer = new Player();
        testPlayer.setName("test");
        testPlayer.setId(3L);
        testPlayer.setToken("test1");
        testPlayer.setShame_tokens(0);
        testPlayer.setIsUser(2L);
        playerRepository.save(testPlayer);
        playerRepository.flush();
        GamePlayer testGamePlayer = new GamePlayer();
        testGamePlayer.setName("tester");
        testGamePlayer.setId(55L);
        testGamePlayer.setShame_tokens(0);
        testGamePlayer.setGame(testGame);
        Set<GamePlayer> players = new HashSet<>();
        players.add(testGamePlayer);
        GameLobby testLobby = new GameLobby();
        testLobby.setPin(222);
        testLobby.setGameid(4L);
        testLobby.setAdmin(testPlayer.getId());
        testLobby.setGamePlayers(null);
        testGamePlayer.setGameLobby(testLobby);
        testLobby.setGamePlayers(players);
        gameLobbyRepository.save(testLobby);
        gameLobbyRepository.flush();

        testLobby = gameLobbyService.createGameLobby(testPlayer);
        testGame = gameService.startGame(testLobby);

        assertEquals(1, testGame.getLevel());
        assertEquals(0, testGame.getSuccessfulMove());
        assertEquals(0, testGame.getCurrentCard());
        // assertEquals(1, testGame.getPlayers().size());
    }

    @Test
    public void startGame_fail() {
        assertEquals(gameRepository.findById(1L), Optional.empty());
        assertEquals(gameLobbyRepository.findById(222), Optional.empty());

        Game testGame = new Game();
        testGame.setId(1L);
        testGame.setGamepin(123456);
        gameRepository.save(testGame);
        gameRepository.flush();
        GameLobby testLobby = new GameLobby();
        testLobby.setPin(222);

        assertThrows(ResponseStatusException.class, () -> gameService.startGame(testLobby));
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
    public void updateGamestatus_success() {
        assertEquals(gameRepository.findById(1L), Optional.empty());

        Game testGame = new Game();
        testGame.setId(99L);
        testGame.setGamepin(123456);
        testGame.setCurrentCard(0);
        Set<GamePlayer> players = new HashSet<>();
        GamePlayer testPlayer = new GamePlayer();
        testPlayer.setName("test");
        testPlayer.setId(7L);
        Set<Integer> cards = new HashSet<>();
        cards.add(27);
        cards.add(28);
        cards.add(29);
        testPlayer.setCards(cards);
        testGame.setSuccessfulMove(1);
        gamePlayerRepository.save(testPlayer);
        gamePlayerRepository.flush();
        players.add(testPlayer);
        testGame.setPlayers(players);
        gameRepository.save(testGame);
        gameRepository.flush();

        System.out.println(testGame);
        System.out.println(gameRepository.findAll());
        System.out.println(testGame.getPlayers());
        System.out.println(gameRepository.findAll().get(0).getPlayers());
        System.out.println(testGame.getPlayingCards());
        System.out.println(gameRepository.findAll().get(0).getPlayingCards());

        gameService.updateGamestatus(gameService.getGame(99L).getId(), 27);

        System.out.println(testGame.getPlayingCards());
        
        assertEquals(27, testGame.getCurrentCard());
    }

    @Test
    public void updateGamestatus_fail() {
        assertEquals(gameRepository.findById(1L), Optional.empty());
        assertThrows(ResponseStatusException.class, () -> gameService.updateGamestatus(1L, 4));
    }

}
