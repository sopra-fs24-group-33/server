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
        // Given
        Player admin = new Player();
        admin.setId(1L);
        admin.setName("Admin");

        GameLobby savedGameLobby = new GameLobby();
        savedGameLobby.setPin(123456); // Set a known pin for verification

        // Mock behavior of the repository
        when(gamelobbyRepository.save(any(GameLobby.class))).thenReturn(savedGameLobby);

        // When
        GameLobby createdGameLobby = gamelobbyService.createGameLobby(admin);

        // Then
        assertNotNull(createdGameLobby);
        assertEquals(admin.getId(), createdGameLobby.getAdmin());
        assertNotNull(createdGameLobby.getPin());
        assertNotEquals(0, createdGameLobby.getPin());

        // Verify that save method of repository was called with the created GameLobby
        verify(gamelobbyRepository, times(1)).save(any(GameLobby.class));
    }
}
