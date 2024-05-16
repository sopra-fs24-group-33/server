package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WebSocketGameHandlerTest {

    private WebSocketGameHandler webSocketGameHandler;
    private GameService gameService;
    private ObjectMapper objectMapper;
    private WebSocketSession session;

    @BeforeEach
    public void setup() {
        gameService = mock(GameService.class);
        objectMapper = new ObjectMapper();
        webSocketGameHandler = new WebSocketGameHandler(gameService, objectMapper);
        session = mock(WebSocketSession.class);
    }

    @Test
    public void testHandleConnectionEstablishedWithParams() throws Exception {
        Map<String, String> queryParams = Map.of("game", "1");
        webSocketGameHandler.handleConnectionEstablishedWithParams(queryParams, session);

        Set<WebSocketSession> sessions = webSocketGameHandler.gameSessions.get(1L);
        assertNotNull(sessions);
        assertTrue(sessions.contains(session));
    }

    @Test
    public void testHandleTextMessage() throws Exception {
        Game game = new Game();
        game.setId(1L);
        when(gameService.getGame(1L)).thenReturn(game);

        String payload = "1";
        TextMessage message = new TextMessage(payload);

        webSocketGameHandler.handleConnectionEstablishedWithParams(Map.of("game", "1"), session);
        webSocketGameHandler.handleTextMessage(session, message);

        ArgumentCaptor<TextMessage> argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session, atLeastOnce()).sendMessage(argumentCaptor.capture());

        TextMessage sentMessage = argumentCaptor.getValue();
        assertTrue(sentMessage.getPayload().contains("\"id\":1"));
    }

    @Test
    public void testBroadcastGameState() throws Exception {
        Game game = new Game();
        game.setId(1L);
        when(gameService.getGame(1L)).thenReturn(game);

        webSocketGameHandler.handleConnectionEstablishedWithParams(Map.of("game", "1"), session);
        webSocketGameHandler.broadcastGameState(1L);

        ArgumentCaptor<TextMessage> argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session, atLeastOnce()).sendMessage(argumentCaptor.capture());

        TextMessage sentMessage = argumentCaptor.getValue();
        assertTrue(sentMessage.getPayload().contains("\"id\":1"));
    }

    @Test
    public void testBroadcastLeaveAll() throws Exception {
        webSocketGameHandler.handleConnectionEstablishedWithParams(Map.of("game", "1"), session);
        webSocketGameHandler.broadcastLeaveAll(1L);

        ArgumentCaptor<TextMessage> argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session, atLeastOnce()).sendMessage(argumentCaptor.capture());

        TextMessage sentMessage = argumentCaptor.getValue();
        assertEquals("leave", sentMessage.getPayload());
    }

    @Test
    public void testAfterConnectionClosed() throws Exception {
        webSocketGameHandler.handleConnectionEstablishedWithParams(Map.of("game", "1"), session);
        webSocketGameHandler.afterConnectionClosed(session, CloseStatus.NORMAL);

        assertTrue(webSocketGameHandler.gameSessions.get(1L).isEmpty());
    }
}
