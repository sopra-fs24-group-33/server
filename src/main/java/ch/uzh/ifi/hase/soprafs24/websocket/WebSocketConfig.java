package ch.uzh.ifi.hase.soprafs24.websocket;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.PlayerService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.WebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Autowired
	public GameLobbyService gameLobbyService;

	@Autowired
	public GameService gameService;

	@Autowired
	public PlayerService playerService;

	@Autowired
	public UserService userService;

	@Autowired
	public ObjectMapper objectMapper;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(lobbyWebSocketHandler(), "/ws/lobby").setAllowedOrigins("*");
		registry.addHandler(gameWebSocketHandler(), "/ws/game").setAllowedOrigins("*");
		registry.addHandler(readyWebSocketHandler(), "ws/ready").setAllowedOrigins("*");
		registry.addHandler(overviewWebSocketHandler(), "/ws/overview").setAllowedOrigins("*");
	}

	@Bean
	public WebSocketHandler overviewWebSocketHandler() {
		return new WebSocketOverviewHandler(objectMapper, playerService, userService);
	}

	@Bean
	public WebSocketHandler lobbyWebSocketHandler() {
		return new WebSocketLobbyHandler(gameLobbyService, objectMapper);
	}

	@Bean
	public WebSocketHandler gameWebSocketHandler() {

		return new WebSocketGameHandler(gameService, objectMapper);
	}

	@Bean
	public WebSocketHandler readyWebSocketHandler() {
		return new WebSocketReadyHandler(objectMapper);
	}
}
