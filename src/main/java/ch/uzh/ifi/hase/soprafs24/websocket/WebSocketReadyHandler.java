package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.service.ReadyStatusService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketReadyHandler extends BaseWebSocketHandler {

	@Autowired
    ReadyStatusService readyStatusService;
	final Map<Integer, Set<WebSocketSession>> lobbySessions = new ConcurrentHashMap<>();

	public WebSocketReadyHandler(ObjectMapper objectMapper) {
		super(objectMapper);
	}

	@Override
	protected void handleConnectionEstablishedWithParams(Map<String, String> queryParams, WebSocketSession session) throws Exception {
		String lobbyPinString = queryParams.get("lobbyPin");
		if (lobbyPinString != null) {
			int lobbyPin = Integer.parseInt(lobbyPinString);
			lobbySessions.computeIfAbsent(lobbyPin, k -> new CopyOnWriteArraySet<>()).add(session);
			broadcastReadyParticipants(lobbyPin);
		}
	}

	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		int totalParticipants = Integer.parseInt(payload);
		Map<String, String> queryParams = parseQueryParams(session.getUri());
		String lobbyPinString = queryParams.get("lobbyPin");
		if (lobbyPinString != null) {
			int lobbyPin = Integer.parseInt(lobbyPinString);
			JsonNode jsonNode = objectMapper.readTree(message.getPayload());
			System.out.println("total participants:"+totalParticipants);
			boolean allReady = readyStatusService.updateReadyStatus(lobbyPin, totalParticipants);
			System.out.println("all ready:" + allReady);

			if (allReady) {
				broadcastAllReady(lobbyPin);
				readyStatusService.resetReadyParticipants(lobbyPin);
			} else {
				broadcastReadyParticipants(lobbyPin);
			}
		}
	}

	void broadcastReadyParticipants(int lobbyPin) {
		int readyParticipants = readyStatusService.getReadyParticipants(lobbyPin);
		String readyParticipantsJson;
		try {
			readyParticipantsJson = objectMapper.writeValueAsString(readyParticipants);
			TextMessage message = new TextMessage(readyParticipantsJson);
			lobbySessions.getOrDefault(lobbyPin, new CopyOnWriteArraySet<>())
					.forEach(s -> {
						try {
							s.sendMessage(message);
						} catch (Exception e) {
							System.err.println("Failed to send message: " + e.getMessage());
						}
					});
		} catch (Exception e) {
			System.err.println("Error serializing ready participants: " + e.getMessage());
		}
	}

	void broadcastAllReady(int lobbyPin) {
		String message = "{\"event\":\"allReady\"}";
		TextMessage textMessage = new TextMessage(message);
		lobbySessions.getOrDefault(lobbyPin, new CopyOnWriteArraySet<>())
				.forEach(session -> {
					try {
						session.sendMessage(textMessage);
					} catch (Exception e) {
						System.err.println("Failed to send all ready message: " + e.getMessage());
					}
				});
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		lobbySessions.values().forEach(sessions -> sessions.remove(session));
	}
}
