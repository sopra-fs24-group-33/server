package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebSocketLobbyHandlerTest {

    private WebSocketLobbyHandler webSocketLobbyHandler;
    private GameLobbyService gameLobbyService;
    private ObjectMapper objectMapper;
    private WebSocketSession session;

    @BeforeEach
    public void setup() {
        gameLobbyService = mock(GameLobbyService.class);
        objectMapper = new ObjectMapper();
        webSocketLobbyHandler = new WebSocketLobbyHandler(gameLobbyService, objectMapper);
        session = mock(WebSocketSession.class);
    }

    @Test
    public void testHandleConnectionEstablishedWithParams() throws Exception {
        GameLobby gameLobby = new GameLobby();
        setPin(gameLobby, 1);
        when(gameLobbyService.getGameLobby(1)).thenReturn(gameLobby);

        Map<String, String> queryParams = Map.of("lobby", "1");
        webSocketLobbyHandler.handleConnectionEstablishedWithParams(queryParams, session);

        Set<WebSocketSession> sessions = webSocketLobbyHandler.lobbySessions.get(1);
        assertNotNull(sessions);
        assertTrue(sessions.contains(session));
    }

    @Test
    public void testBroadcastLobbyState() throws Exception {
        GameLobby gameLobby = new GameLobby();
        setPin(gameLobby, 1);

        GamePlayer player = new GamePlayer();
        gameLobby.setGamePlayers(List.of(player));
        when(gameLobbyService.getGameLobby(1)).thenReturn(gameLobby);

        webSocketLobbyHandler.handleConnectionEstablishedWithParams(Map.of("lobby", "1"), session);
        webSocketLobbyHandler.broadcastLobbyState(1);

        ArgumentCaptor<TextMessage> argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session, atLeastOnce()).sendMessage(argumentCaptor.capture());

        TextMessage sentMessage = argumentCaptor.getValue();
        assertTrue(sentMessage.getPayload().contains("\"pin\":1"));
    }

    @Test
    public void testBroadcastLeaveAll() throws Exception {
        GameLobby gameLobby = new GameLobby();
        setPin(gameLobby, 1);
        when(gameLobbyService.getGameLobby(1)).thenReturn(gameLobby);

        webSocketLobbyHandler.handleConnectionEstablishedWithParams(Map.of("lobby", "1"), session);
        webSocketLobbyHandler.broadcastLeaveAll(1);

        ArgumentCaptor<TextMessage> argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session, atLeastOnce()).sendMessage(argumentCaptor.capture());

        TextMessage sentMessage = argumentCaptor.getValue();
        assertEquals("leave", sentMessage.getPayload());
    }

    @Test
    public void testHandleTextMessage() throws Exception {
        GameLobby gameLobby = new GameLobby();
        setPin(gameLobby, 1);
        when(gameLobbyService.getGameLobby(1)).thenReturn(gameLobby);

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("action", "join");
        jsonMessage.put("lobbyPin", 1);

        TextMessage message = new TextMessage(jsonMessage.toString());

        webSocketLobbyHandler.handleConnectionEstablishedWithParams(Map.of("lobby", "1"), session);
        webSocketLobbyHandler.handleTextMessage(session, message);

        ArgumentCaptor<TextMessage> argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session, atLeastOnce()).sendMessage(argumentCaptor.capture());

        TextMessage sentMessage = argumentCaptor.getValue();
        assertTrue(sentMessage.getPayload().contains("\"pin\":1"));
    }

    @Test
    public void testAfterConnectionClosed() throws Exception {
        GameLobby gameLobby = new GameLobby();
        setPin(gameLobby, 1);
        when(gameLobbyService.getGameLobby(1)).thenReturn(gameLobby);

        webSocketLobbyHandler.handleConnectionEstablishedWithParams(Map.of("lobby", "1"), session);
        webSocketLobbyHandler.afterConnectionClosed(session, CloseStatus.NORMAL);

        assertTrue(webSocketLobbyHandler.lobbySessions.get(1).isEmpty());
    }

    private void setPin(GameLobby gameLobby, int pin) throws Exception {
        java.lang.reflect.Field pinField = gameLobby.getClass().getDeclaredField("pin");
        pinField.setAccessible(true);
        pinField.set(gameLobby, pin);
    }
}
