package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.constant.Role;
import ch.uzh.ifi.hase.soprafs24.agoratoken.RtcTokenBuilder;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AgoraServiceTest {

    @InjectMocks
    private AgoraService agoraService;

    @Mock
    private RtcTokenBuilder rtcTokenBuilder;

    private Player testPlayer;
    private GameLobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize test objects
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setName("testPlayername");

        testLobby = new GameLobby();
        testLobby.setAdmin(testPlayer.getId());
        testLobby.setPin(111111);

        // Stubbing for the mock rtcTokenBuilder
        when(rtcTokenBuilder.buildTokenWithUserAccount(anyString(), anyString(), any(Role.class)))
                .thenReturn("mockToken");
    }

    @Test
    public void testCreateToken_Success() {
        // Call the method to test
        String result = agoraService.createToken(1, 123L);

        // Verify the result
        assertEquals("mockToken", result);

        // Verify that the buildTokenWithUserAccount method was called once
        verify(rtcTokenBuilder, times(1)).buildTokenWithUserAccount(eq("1"), eq("123"), eq(Role.Role_Publisher));
    }

    @Test
    public void testCreateToken_NotFound() {
        // Stubbing for the mock rtcTokenBuilder to return an empty string
        when(rtcTokenBuilder.buildTokenWithUserAccount(anyString(), anyString(), any(Role.class)))
                .thenReturn("");

        // Call the method to test and expect an exception
        assertThrows(ResponseStatusException.class, () -> {
            agoraService.createToken(1, 123L);
        });

        // Verify that the buildTokenWithUserAccount method was called once
        verify(rtcTokenBuilder, times(1)).buildTokenWithUserAccount(eq("1"), eq("123"), eq(Role.Role_Publisher));
    }
}