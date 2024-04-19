package ch.uzh.ifi.hase.soprafs24.websocket;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameLobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Autowired
	private GameLobbyService gameLobbyService;

	@Autowired
	private ObjectMapper objectMapper;

	public WebSocketConfig(GameLobbyService gameLobbyService) {
		this.gameLobbyService = gameLobbyService;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(lobbyWebSocketHandler(), "/ws/lobby").setAllowedOrigins("*");
	}

	@Bean
	public WebSocketHandler lobbyWebSocketHandler() {
		return new WebSocketLobbyHandler(gameLobbyService, objectMapper);
	}

	public class MyWebSocketHandler extends TextWebSocketHandler {
		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			sessions.add(session);
			System.out.println("New session added: " + session.getId());
		}

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
			JSONObject jsonMessage = new JSONObject(message.getPayload());
			switch (jsonMessage.getString("action")) {
				case "fetchLobby":
					int pin = jsonMessage.getInt("pin");
					GameLobby gameLobby = gameLobbyService.getGameLobby(pin);
					GameLobbyGetDTO lobbyDTO = DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(gameLobby);

					// Send the lobby data back to the client
					String response = new JSONObject()
							.put("action", "updateLobby")
							.put("lobby", new JSONObject(lobbyDTO)) // Ensure you have a way to serialize DTOs to JSON
							.toString();
					session.sendMessage(new TextMessage(response));
					break;
				// other cases as needed
			}
			System.out.println("Received message: " + message.getPayload() + " from " + session.getId());
			// Broadcast the message to all other sessions
			for (WebSocketSession webSocketSession : sessions) {
				if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
					webSocketSession.sendMessage(message);
				}
			}
		}


		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			sessions.remove(session);
			System.out.println("Session removed: " + session.getId());
		}
	}
}
