package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.repository.GameLobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GameLobbyResource REST resource.
 *
 * @see GameLobbyService
 */
@WebAppConfiguration
@SpringBootTest
public class GameLobbyServiceIntegrationTest {

    @Qualifier("gamelobbyRepository")
    @Autowired
    private GameLobbyRepository gamelobbyRepository;

    @Autowired
    private GameLobbyService gamelobbyService;

    @BeforeEach
    public void setup() {
        gamelobbyRepository.deleteAll();
    }

    @Test
    public void createGameLobby_validInputs_success() {
        // given
        Player admin = new Player();
        admin.setId(1L);
        admin.setName("test");

        GameLobby testGameLobby = new GameLobby();
        testGameLobby.setAdmin(admin.getId());

        // when
        GameLobby createdGameLobby = gamelobbyService.createGameLobby(admin);

        // then
        assertEquals(testGameLobby.getAdmin(), createdGameLobby.getAdmin());
        assertNotNull(createdGameLobby.getPin());
    }

    @Test
    public void addPlayer_validInputs_success() {
        // given
        Player admin = new Player();
        admin.setId(1L);
        admin.setName("test");

        Player player = new Player();
        player.setId(2L);
        player.setName("test");

        GameLobby testGameLobby = new GameLobby();
        testGameLobby.setAdmin(admin.getId());

        // when
        GameLobby createdGameLobby = gamelobbyService.createGameLobby(admin);
        createdGameLobby = gamelobbyService.addPlayer(player, createdGameLobby);

        assertNotNull(createdGameLobby);
        assertEquals(2, createdGameLobby.getGamePlayers().size());
    }

    @Test
    public void removePlayer_validInputs_success() {
        // given
        Player admin = new Player();
        admin.setId(1L);
        admin.setName("test");

        Player player = new Player();
        player.setId(2L);
        player.setName("test");

        GameLobby testGameLobby = new GameLobby();
        testGameLobby.setAdmin(admin.getId());

        // when
        GameLobby createdGameLobby = gamelobbyService.createGameLobby(admin);
        createdGameLobby = gamelobbyService.addPlayer(player, createdGameLobby);

        createdGameLobby = gamelobbyService.removePlayer(player, createdGameLobby);

        assertNotNull(createdGameLobby);
        assertEquals(1, createdGameLobby.getGamePlayers().size());
    }

}