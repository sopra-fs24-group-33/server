package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketGameHandler extends TextWebSocketHandler {

	private final Map<Integer, Set<WebSocketSession>> lobbySessions = new ConcurrentHashMap<>();
	private final GameService gameService;
	private final ObjectMapper objectMapper;
	public WebSocketGameHandler(GameService gameService, ObjectMapper objectMapper) {
		this.gameService = gameService;
		this.objectMapper = objectMapper;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("New game WebSocket connection:" + session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("Received game messsage: " + message.getPayload());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("Game WebSocket connection closed: " + session.getId());
	}
}
