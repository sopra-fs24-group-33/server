package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketGameHandler extends BaseWebSocketHandler {

	private final GameService gameService;
	final Map<Long, Set<WebSocketSession>> gameSessions = new ConcurrentHashMap<>();
	public WebSocketGameHandler(GameService gameService, ObjectMapper objectMapper) {
		super(objectMapper);
		this.gameService = gameService;
	}

	@Override
	protected void handleConnectionEstablishedWithParams(Map<String, String> queryParams, WebSocketSession session) throws Exception {
		String gameIdString = queryParams.get("game");
		if (gameIdString != null) {
			long gameId = Long.parseLong(gameIdString);
			gameSessions.computeIfAbsent(gameId, k -> new CopyOnWriteArraySet<>()).add(session);
		}
	}
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		System.out.println("Received game messsage: " + payload);
		if (payload.equals("returnToLobby")) {
			gameSessions.forEach((gameId, sessions) -> {
				if (sessions.remove(session) && !sessions.isEmpty()) {
					broadcastReturnToLobby(gameId);
				}
			});
		} else {
			long gameId = Long.parseLong(payload);
			broadcastGameState(gameId);
		}
	}

	void broadcastGameState(long gameId) throws Exception {
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

	void broadcastLeaveAll(long gameId) {
		TextMessage message = new TextMessage("leave");

		gameSessions.getOrDefault(gameId, new CopyOnWriteArraySet<>())
				.forEach(s -> {
					try {
						s.sendMessage(message);
					} catch (Exception e) {
						System.err.println("Failed to send message: " + e.getMessage());
					}
				});
	}

	void broadcastReturnToLobby(long gameId) {
		TextMessage message = new TextMessage("return");

		gameSessions.getOrDefault(gameId, new CopyOnWriteArraySet<>())
				.forEach(s -> {
					try {
						s.sendMessage(message);
					} catch (Exception e) {
						System.err.println("Failed to send message: " + e.getMessage());
					}
				});
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		gameSessions.forEach((gameId, sessions) -> {
			if (sessions.remove(session) && !sessions.isEmpty()) {
				try {
					// If connection closed delete the game
					gameService.deleteGame(gameId);
					broadcastLeaveAll(gameId);
				} catch (Exception e) {
					// If game already deleted skip delete part
					broadcastLeaveAll(gameId);
				}
			}
		});
		System.out.println("Removed session from game: Session ID " + session.getId());
	}

}


