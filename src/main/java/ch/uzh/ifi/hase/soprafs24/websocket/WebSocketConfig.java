package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private GameLobbyService gameLobbyService;

	private final Map<Integer, Set<WebSocketSession>> lobbySessions = new ConcurrentHashMap<>();

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(gameLobbyWebSocketHandler(), "/ws").setAllowedOrigins("*");
	}

	// Helper method to parse query parameters
	private Map<String, String> parseQueryParams(URI uri) {
		Map<String, String> queryParams = new HashMap<>();
		String query = uri.getQuery();
		if (query != null) {
			String[] pairs = query.split("&");
			for (String pair : pairs) {
				int idx = pair.indexOf("=");
				queryParams.put(pair.substring(0, idx), pair.substring(idx + 1));
			}
		}
		return queryParams;
	}
	@Bean
	public WebSocketHandler gameLobbyWebSocketHandler() {
		return new TextWebSocketHandler() {
			@Override
			public void afterConnectionEstablished(WebSocketSession session) throws Exception {
				URI uri = session.getUri();
				if (uri != null) {
					Map<String, String> queryParams = parseQueryParams(uri);
					String lobbyPinString = queryParams.get("lobby");
					if (lobbyPinString != null) {
						int gamePin = Integer.parseInt(lobbyPinString);
						lobbySessions.computeIfAbsent(gamePin, k -> new CopyOnWriteArraySet<>()).add(session);
						GameLobby gameLobby = gameLobbyService.getGameLobby(gamePin);
						String gameLobbyJson = DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(gameLobby).toString();
						System.out.println("Sending Game Lobby data: " + gameLobbyJson); // Debug statement
						session.sendMessage(new TextMessage(gameLobbyJson));
					}
				}
			}

			@Override
			protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
				// Handle incoming messages if needed
			}

			@Override
			public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
				lobbySessions.forEach((pin, sessions) -> sessions.remove(session));
			}
		};
	}

	public void broadcastUpdate(Integer gamePin, GameLobby gameLobby) throws IOException {
		TextMessage message = new TextMessage(DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(gameLobby).toString());
		Set<WebSocketSession> sessions = lobbySessions.getOrDefault(gamePin, new CopyOnWriteArraySet<>());
		for (WebSocketSession session : sessions) {
			if (session.isOpen()) {
				session.sendMessage(message);
			}
		}
	}
}
