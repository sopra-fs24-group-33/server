package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
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

public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

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




}