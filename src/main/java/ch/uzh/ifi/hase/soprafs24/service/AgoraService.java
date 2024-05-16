package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.agoratoken.RtcTokenBuilder;
import ch.uzh.ifi.hase.soprafs24.constant.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class AgoraService {

    private final RtcTokenBuilder rtcTokenBuilder;

    public AgoraService(RtcTokenBuilder rtcTokenBuilder) {
        this.rtcTokenBuilder = rtcTokenBuilder;
    }

    public String createToken(Integer lobbyId, Long playerId) {
        String token = rtcTokenBuilder.buildTokenWithUserAccount(Integer.toString(lobbyId), playerId.toString(), Role.Role_Publisher);
        if (Objects.equals(token, "")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return token;
    }
}
