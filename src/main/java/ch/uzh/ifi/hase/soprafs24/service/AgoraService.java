package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.agoratoken.RtcTokenBuilder;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.constant.Role;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;

@Service
public class AgoraService {
    public String createToken(Integer lobbyId, Long playerId) {
        RtcTokenBuilder newToken = new RtcTokenBuilder();
        String token = newToken.buildTokenWithUserAccount(Integer.toString(lobbyId), playerId.toString(), Role.Role_Publisher);
        if (Objects.equals(token, "")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return token;
    }
}