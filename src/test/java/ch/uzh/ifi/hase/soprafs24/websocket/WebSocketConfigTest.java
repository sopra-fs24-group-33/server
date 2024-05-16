package ch.uzh.ifi.hase.soprafs24.websocket;
import ch.uzh.ifi.hase.soprafs24.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.WebSocketHandler;

import static org.mockito.Mockito.*;

public class WebSocketConfigTest {

    @Test
    public void testRegisterWebSocketHandlers() {
        WebSocketHandlerRegistry registry = Mockito.mock(WebSocketHandlerRegistry.class);
        WebSocketHandlerRegistration registration = Mockito.mock(WebSocketHandlerRegistration.class);
        WebSocketConfig webSocketConfig = new WebSocketConfig();
        when(registry.addHandler(any(WebSocketHandler.class), anyString())).thenReturn(registration);
        webSocketConfig.registerWebSocketHandlers(registry);
        verify(registry, times(1)).addHandler(any(WebSocketHandler.class), eq("/ws/lobby"));
        verify(registry, times(1)).addHandler(any(WebSocketHandler.class), eq("/ws/game"));
        verify(registry, times(1)).addHandler(any(WebSocketHandler.class), eq("ws/ready"));
        verify(registry, times(1)).addHandler(any(WebSocketHandler.class), eq("/ws/overview"));
    }

    @Test
    public void testAllowedOrigins() {
        WebSocketHandlerRegistration registration = Mockito.mock(WebSocketHandlerRegistration.class);
        WebSocketHandlerRegistry registry = Mockito.mock(WebSocketHandlerRegistry.class);
        when(registry.addHandler(any(WebSocketHandler.class), anyString())).thenReturn(registration);
        when(registration.setAllowedOrigins("*")).thenReturn(registration);
        WebSocketConfig webSocketConfig = new WebSocketConfig();
        webSocketConfig.registerWebSocketHandlers(registry);
        verify(registry, times(4)).addHandler(any(WebSocketHandler.class), anyString());
        verify(registration, times(4)).setAllowedOrigins("*");
    }

    @Test
    public void testWebSocketHandlerBeans() {
        GameLobbyService gameLobbyService = mock(GameLobbyService.class);
        GameService gameService = mock(GameService.class);
        PlayerService playerService = mock(PlayerService.class);
        UserService userService = mock(UserService.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        WebSocketConfig webSocketConfig = new WebSocketConfig();
        webSocketConfig.gameLobbyService = gameLobbyService;
        webSocketConfig.gameService = gameService;
        webSocketConfig.playerService = playerService;
        webSocketConfig.userService = userService;
        webSocketConfig.objectMapper = objectMapper;
        WebSocketHandler overviewWebSocketHandler = webSocketConfig.overviewWebSocketHandler();
        WebSocketHandler lobbyWebSocketHandler = webSocketConfig.lobbyWebSocketHandler();
        WebSocketHandler gameWebSocketHandler = webSocketConfig.gameWebSocketHandler();
        WebSocketHandler readyWebSocketHandler = webSocketConfig.readyWebSocketHandler();
        assert overviewWebSocketHandler != null;
        assert lobbyWebSocketHandler != null;
        assert gameWebSocketHandler != null;
        assert readyWebSocketHandler != null;
    }
}

