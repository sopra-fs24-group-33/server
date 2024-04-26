package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
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

public class GameServiceTest {
    
    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game testGame;
    private GamePlayer testPlayer1;
    private GamePlayer testPlayer2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testPlayer1 = new GamePlayer();
        testPlayer1.setName("player1");
        Set<Integer> cards1 = new HashSet<Integer>();
        cards1.add(1);
        cards1.add(2);
        testPlayer1.setCards(cards1);

        testPlayer2 = new GamePlayer();
        testPlayer2.setName("player2");
        Set<Integer> cards2 = new HashSet<Integer>();
        cards2.add(3);
        cards2.add(4);
        testPlayer2.setCards(cards2);

        Set<GamePlayer> testPlayers = new HashSet<GamePlayer>();
        testPlayers.add(testPlayer1);
        testPlayers.add(testPlayer2);

        testGame = new Game();
        testGame.setId(123456L);
        testGame.setPlayers(testPlayers);

        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }

    @Test
    public void startGame_success() {
        Game createdGame = gameRepository.save(testGame);
    }

    @Test
    public void doRound_success() {
        Game createdGame = gameRepository.save(testGame);
    }
}
