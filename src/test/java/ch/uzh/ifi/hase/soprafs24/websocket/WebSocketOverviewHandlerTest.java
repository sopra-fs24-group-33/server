package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.PlayerService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebSocketOverviewHandlerTest {

    private WebSocketOverviewHandler webSocketOverviewHandler;
    private PlayerService playerService;
    private UserService userService;
    private ObjectMapper objectMapper;
    private WebSocketSession session;

    @BeforeEach
    public void setup() {
        playerService = mock(PlayerService.class);
        userService = mock(UserService.class);
        objectMapper = new ObjectMapper();
        webSocketOverviewHandler = new WebSocketOverviewHandler(objectMapper, playerService, userService);
        session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn("session1");
    }

    @Test
    public void testHandleConnectionEstablishedWithParams() throws Exception {
        Map<String, String> queryParams = new HashMap<>();
        webSocketOverviewHandler.handleConnectionEstablishedWithParams(queryParams, session);

        Set<WebSocketSession> sessions = webSocketOverviewHandler.overviewSessions;
        assertNotNull(sessions);
        assertTrue(sessions.contains(session));
    }

    @Test
    public void testHandleTextMessage() throws Exception {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("playerId", 1L);

        TextMessage message = new TextMessage(jsonMessage.toString());

        webSocketOverviewHandler.handleTextMessage(session, message);

        assertEquals(1L, webSocketOverviewHandler.sessionPlayerMap.get(session.getId()));
    }

    @Test
    public void testAfterConnectionClosed() throws Exception {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("playerId", 1L);

        TextMessage message = new TextMessage(jsonMessage.toString());
        webSocketOverviewHandler.handleTextMessage(session, message);

        webSocketOverviewHandler.afterConnectionClosed(session, CloseStatus.GOING_AWAY);

        verify(playerService, times(1)).logoutUser(1L);
        assertFalse(webSocketOverviewHandler.overviewSessions.contains(session));
    }

    @Test
    public void testAfterConnectionClosedWithoutPlayerId() throws Exception {
        webSocketOverviewHandler.afterConnectionClosed(session, CloseStatus.NORMAL);

        verify(playerService, never()).logoutUser(anyLong());
        assertFalse(webSocketOverviewHandler.overviewSessions.contains(session));
    }

    @Test
    public void testAfterConnectionClosedWithLogoutReason() throws Exception {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("playerId", 1L);

        TextMessage message = new TextMessage(jsonMessage.toString());
        webSocketOverviewHandler.handleTextMessage(session, message);

        CloseStatus logoutStatus = new CloseStatus(1000, "logout");
        webSocketOverviewHandler.afterConnectionClosed(session, logoutStatus);

        verify(playerService, times(1)).logoutUser(1L);
        assertFalse(webSocketOverviewHandler.overviewSessions.contains(session));
    }
}
