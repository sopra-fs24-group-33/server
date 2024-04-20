package ch.uzh.ifi.hase.soprafs24.websocket;// WebSocketLobbyHandler.java

import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.json.JSONObject;
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
				broadcastLobbyState(lobbyPin);
			}
		}
	}

	private void broadcastLobbyState(int lobbyPin) throws Exception {
		GameLobby gameLobby = gameLobbyService.getGameLobby(lobbyPin);
		for (GamePlayer player : gameLobby.getGamePlayers()) {
			Hibernate.initialize(player.getCards()); // Initialize lazy-loaded collections
		}
		String lobbyState = objectMapper.writeValueAsString(DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(gameLobby));
		TextMessage message = new TextMessage(lobbyState);

		lobbySessions.getOrDefault(lobbyPin, new CopyOnWriteArraySet<>())
				.forEach(s -> {
					try {
						s.sendMessage(message);
					} catch (Exception e) {
						System.err.println("Failed to send message: " + e.getMessage());
					}
				});
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		System.out.println("message:" + message.getPayload());
		System.out.println("message type:" + message.getClass().getName());

		// Handle incoming messages, such as a player joining
		JSONObject jsonMessage = new JSONObject(message.getPayload());
		if ("join".equals(jsonMessage.getString("action"))) {
			int lobbyPin = jsonMessage.getInt("lobbyPin");
			// Update lobby state with the new player details (not shown here)
			broadcastLobbyState(lobbyPin); // Re-broadcast the updated lobby state to all clients
		}
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
			if (sessions.remove(session) && !sessions.isEmpty()) {
				try {
					broadcastLobbyState(pin);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		System.out.println("Removed session from lobby: Session ID " + session.getId());
	}
}
