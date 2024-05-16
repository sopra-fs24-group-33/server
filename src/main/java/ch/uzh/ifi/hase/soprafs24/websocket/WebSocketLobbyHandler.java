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

public class WebSocketLobbyHandler extends BaseWebSocketHandler {

	private final GameLobbyService gameLobbyService;
	final Map<Integer, Set<WebSocketSession>> lobbySessions = new ConcurrentHashMap<>();

	public WebSocketLobbyHandler(GameLobbyService gameLobbyService, ObjectMapper objectMapper) {
		super(objectMapper);
		this.gameLobbyService = gameLobbyService;
	}

	@Override
	protected void handleConnectionEstablishedWithParams(Map<String, String> queryParams, WebSocketSession session) throws Exception {
		String lobbyPinString = queryParams.get("lobby");
		if (lobbyPinString != null) {
			int lobbyPin = Integer.parseInt(lobbyPinString);
			lobbySessions.computeIfAbsent(lobbyPin, k -> new CopyOnWriteArraySet<>()).add(session);
			broadcastLobbyState(lobbyPin);
		}
	}

	void broadcastLobbyState(int lobbyPin) throws Exception {
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

	void broadcastLeaveAll(int lobbyPin) {
		TextMessage message = new TextMessage("leave");

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
		System.out.println("Received message: " + message.getPayload());
		JSONObject jsonMessage = new JSONObject(message.getPayload());
		if ("join".equals(jsonMessage.getString("action"))) {
			int lobbyPin = jsonMessage.getInt("lobbyPin");
			broadcastLobbyState(lobbyPin); // Re-broadcast the updated lobby state to all clients
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		lobbySessions.forEach((pin, sessions) -> {
			if (sessions.remove(session) && !sessions.isEmpty()) {
				try {
					broadcastLobbyState(pin);
				} catch (Exception e) {
					broadcastLeaveAll(pin);
				}
			}
		});
		System.out.println("Removed session from lobby: Session ID " + session.getId());
	}
}
