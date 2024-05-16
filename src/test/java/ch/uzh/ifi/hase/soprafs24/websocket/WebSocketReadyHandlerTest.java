package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.service.ReadyStatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebSocketReadyHandlerTest {

    private WebSocketReadyHandler webSocketReadyHandler;
    private ReadyStatusService readyStatusService;
    private ObjectMapper objectMapper;
    private WebSocketSession session;

    @BeforeEach
    public void setup() {
        readyStatusService = mock(ReadyStatusService.class);
        objectMapper = new ObjectMapper();
        webSocketReadyHandler = new WebSocketReadyHandler(objectMapper);
        webSocketReadyHandler.readyStatusService = readyStatusService;
        session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn("session1");
    }

    @Test
    public void testHandleConnectionEstablishedWithParams() throws Exception {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lobbyPin", "1234");

        webSocketReadyHandler.handleConnectionEstablishedWithParams(queryParams, session);

        Set<WebSocketSession> sessions = webSocketReadyHandler.lobbySessions.get(1234);
        assertNotNull(sessions);
        assertTrue(sessions.contains(session));
        verify(readyStatusService, times(1)).getReadyParticipants(1234);
    }

    @Test
    public void testHandleTextMessage() throws Exception {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lobbyPin", "1234");
        when(session.getUri()).thenReturn(new java.net.URI("ws://localhost?lobbyPin=1234"));

        webSocketReadyHandler.handleConnectionEstablishedWithParams(queryParams, session);

        when(readyStatusService.updateReadyStatus(1234, 5)).thenReturn(false);

        TextMessage message = new TextMessage("5");
        webSocketReadyHandler.handleTextMessage(session, message);

        verify(readyStatusService, times(1)).updateReadyStatus(1234, 5);
        verify(readyStatusService, never()).resetReadyParticipants(1234);
    }

    @Test
    public void testHandleTextMessageAllReady() throws Exception {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lobbyPin", "1234");
        when(session.getUri()).thenReturn(new java.net.URI("ws://localhost?lobbyPin=1234"));

        webSocketReadyHandler.handleConnectionEstablishedWithParams(queryParams, session);

        when(readyStatusService.updateReadyStatus(1234, 5)).thenReturn(true);

        TextMessage message = new TextMessage("5");
        webSocketReadyHandler.handleTextMessage(session, message);

        verify(readyStatusService, times(1)).updateReadyStatus(1234, 5);
        verify(readyStatusService, times(1)).resetReadyParticipants(1234);
    }

    @Test
    public void testAfterConnectionClosed() throws Exception {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lobbyPin", "1234");
        webSocketReadyHandler.handleConnectionEstablishedWithParams(queryParams, session);

        webSocketReadyHandler.afterConnectionClosed(session, CloseStatus.NORMAL);

        Set<WebSocketSession> sessions = webSocketReadyHandler.lobbySessions.get(1234);
        assertFalse(sessions.contains(session));
    }

    @Test
    public void testBroadcastReadyParticipants() throws Exception {
        webSocketReadyHandler.lobbySessions.computeIfAbsent(1234, k -> new CopyOnWriteArraySet<>()).add(session);
        when(readyStatusService.getReadyParticipants(1234)).thenReturn(3);

        webSocketReadyHandler.broadcastReadyParticipants(1234);

        ArgumentCaptor<TextMessage> argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session, atLeastOnce()).sendMessage(argumentCaptor.capture());

        TextMessage sentMessage = argumentCaptor.getValue();
        String payload = sentMessage.getPayload();
        assertEquals("3", payload);
    }

    @Test
    public void testBroadcastAllReady() throws Exception {
        webSocketReadyHandler.lobbySessions.computeIfAbsent(1234, k -> new CopyOnWriteArraySet<>()).add(session);

        webSocketReadyHandler.broadcastAllReady(1234);

        ArgumentCaptor<TextMessage> argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session, atLeastOnce()).sendMessage(argumentCaptor.capture());

        TextMessage sentMessage = argumentCaptor.getValue();
        String payload = sentMessage.getPayload();
        assertEquals("{\"event\":\"allReady\"}", payload);
    }
}
