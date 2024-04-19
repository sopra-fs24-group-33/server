package ch.uzh.ifi.hase.soprafs24.websocket;// WebSocketLobbyHandler.java

import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketLobbyHandler extends TextWebSocketHandler {

	private final Map<Integer, Set<WebSocketSession>> lobbySessions = new ConcurrentHashMap<>();
	private final GameLobbyService gameLobbyService;
	private final ObjectMapper objectMapper;

	public WebSocketLobbyHandler(GameLobbyService gameLobbyService, ObjectMapper objectMapper) {
		this.gameLobbyService = gameLobbyService;
		this.objectMapper = objectMapper;
	}

	private Map<String, String> parseQueryParams(URI uri) {
		String query = uri.getQuery();
		Map<String, String> queryParams = new HashMap<>();
		if (query != null) {
			for (String param : query.split("&")) {
				String[] entry = param.split("=");
				if (entry.length > 1) {
					queryParams.put(entry[0], entry[1]);
				} else {
					queryParams.put(entry[0], "");
				}
			}
		}
		return queryParams;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("Added session from lobby: Session ID " + session.getId());
		URI uri = session.getUri();
		if (uri != null) {
			Map<String, String> queryParams = parseQueryParams(uri);
			String lobbyPinString = queryParams.get("lobby");
			if (lobbyPinString != null) {
				int lobbyPin = Integer.parseInt(lobbyPinString);
				lobbySessions.computeIfAbsent(lobbyPin, k -> new CopyOnWriteArraySet<>()).add(session);
				GameLobby gameLobby = gameLobbyService.getGameLobby(lobbyPin);
				String lobbyState = objectMapper.writeValueAsString(DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(gameLobby));
				System.out.println("xD.");
				session.sendMessage(new TextMessage(lobbyState));
			}
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// Logic to handle incoming messages
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// Log the error
		System.err.println("WebSocket transport error: " + exception.getMessage());

		// Optional: Add additional error handling logic here
		// For example, you might want to close the session if the error is severe:
		if (session.isOpen()) {
			session.close(CloseStatus.SERVER_ERROR.withReason("Transport error"));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		lobbySessions.forEach((pin, sessions) -> {
			sessions.remove(session);
			if (sessions.isEmpty()) {
				lobbySessions.remove(pin);
			}
		});
		System.out.println("Removed session from lobby: Session ID " + session.getId());
	}
}
