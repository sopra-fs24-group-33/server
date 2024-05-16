package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.PlayerService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class WebSocketOverviewHandler extends BaseWebSocketHandler {

	private final PlayerService playerService;
	private final UserService userService;
	final Set<WebSocketSession> overviewSessions = new CopyOnWriteArraySet<>();
	final Map<String, Long> sessionPlayerMap = new ConcurrentHashMap<>();

	public WebSocketOverviewHandler(ObjectMapper objectMapper, PlayerService playerService, UserService userService) {
		super(objectMapper);
		this.playerService = playerService;
		this.userService = userService;
	}


	@Override
	protected void handleConnectionEstablishedWithParams(Map<String, String> queryParams, WebSocketSession session) throws Exception {
		overviewSessions.add(session);
		broadcastOverview();
	}


	void broadcastOverview() throws Exception {
		// Gather player data and convert to DTOs
		List<Player> players = playerService.getPlayers();
		List<PlayerGetDTO> playerDTOs = players.stream()
				.map(DTOMapper.INSTANCE::convertEntityToPlayerGetDTO)
				.collect(Collectors.toList());

		// Gather user data and convert to DTOs
		List<User> users = userService.getUsers();
		List<UserGetDTO> userDTOs = users.stream()
				.map(DTOMapper.INSTANCE::convertEntityToUserGetDTO)
				.collect(Collectors.toList());

		// Creating a combined JSON object
		Map<String, Object> overviewMap = new HashMap<>();
		overviewMap.put("players", playerDTOs);
		overviewMap.put("users", userDTOs);
		String overviewJson = objectMapper.writeValueAsString(overviewMap);

		TextMessage overviewMessage = new TextMessage(overviewJson);
		overviewSessions.forEach(session -> {
			try {
				session.sendMessage(overviewMessage);
			} catch (Exception e) {
				System.err.println("Failed to send message: " + e.getMessage());
			}
		});
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		System.out.println("Payload:" + message.getPayload());
		JSONObject jsonMessage = new JSONObject(message.getPayload());
		if (jsonMessage.has("playerId")) {
			Long userId = jsonMessage.getLong("playerId");
			sessionPlayerMap.put(session.getId(), userId);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Long playerId = sessionPlayerMap.remove(session.getId());
		if (status.equalsCode(CloseStatus.GOING_AWAY) || Objects.equals(status.getReason(), "logout")) {
			if (playerId != null) {
				playerService.logoutUser(playerId);
			}
		}
		overviewSessions.remove(session);
		System.out.println("Removed session from overview: Session ID " + session.getId());
		broadcastOverview();
	}
}
