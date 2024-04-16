package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyGetDTO {

    private Long admin;

    private int pin;

    private List<GamePlayer> gamePlayers = new ArrayList<>();

    private Long gameid;

    public Long getAdmin() {
        return admin;
    }

    public void setAdmin(Long admin) {
        this.admin = admin;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public List<GamePlayer> getPlayers() {
        return gamePlayers;
    }

    public void setPlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
    public Long getGameid() {
        return gameid;
    }
    public void setGameid(Long gameid) {
        this.gameid = gameid;
    }
}
