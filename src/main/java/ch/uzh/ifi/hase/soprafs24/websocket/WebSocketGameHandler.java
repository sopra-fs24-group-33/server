package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketGameHandler extends TextWebSocketHandler {

	private final Map<Long, Set<WebSocketSession>> gameSessions = new ConcurrentHashMap<>();
	private final GameService gameService;
	private final ObjectMapper objectMapper;
	public WebSocketGameHandler(GameService gameService, ObjectMapper objectMapper) {
		this.gameService = gameService;
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
		System.out.println("New game WebSocket connection:" + session.getId());
		URI uri = session.getUri();
		if (uri != null) {
			Map<String, String> queryParams = parseQueryParams(uri);
			String gameIdString = queryParams.get("game");
			if (gameIdString != null) {
				long gameId = Long.parseLong(gameIdString);
				gameSessions.computeIfAbsent(gameId, k -> new CopyOnWriteArraySet<>()).add(session);
			}
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		System.out.println("Received game messsage: " + payload);
		long gameId = Long.parseLong(payload);
		broadcastGameState(gameId);

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("Game WebSocket connection closed: " + session.getId());
	}

	private void broadcastGameState(long gameId) throws Exception {
		Game game = gameService.getGame(gameId);
		String gameState = objectMapper.writeValueAsString(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
		TextMessage message = new TextMessage(gameState);

		gameSessions.getOrDefault(gameId, new CopyOnWriteArraySet<>())
				.forEach(s -> {
					try {
						s.sendMessage(message);
					} catch (Exception e) {
						System.err.println("Failed to send message: " + e.getMessage());
					}
				});
	}
}
