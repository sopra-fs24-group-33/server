package ch.uzh.ifi.hase.soprafs24.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseWebSocketHandler extends TextWebSocketHandler {

	protected final ObjectMapper objectMapper;

	public BaseWebSocketHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	protected Map<String, String> parseQueryParams(URI uri) {
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
		System.out.println("WebSocket connection established: " + session.getId());
		URI uri = session.getUri();
		if (uri != null) {
			Map<String, String> queryParams = parseQueryParams(uri);
			handleConnectionEstablishedWithParams(queryParams, session);
		}
	}

	protected abstract void handleConnectionEstablishedWithParams(Map<String, String> queryParams, WebSocketSession session) throws Exception;

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("WebSocket connection closed: " + session.getId());
	}
}

