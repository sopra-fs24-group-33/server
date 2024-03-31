package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyPostDTO {

    private Long id;

    private Long admin;

    private String pin;

    private List<GamePlayer> gamePlayers = new ArrayList<>();

    private Game gamestatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdmin() {
        return admin;
    }

    public void setAdmin(Long admin) {
        this.admin = admin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public List<GamePlayer> getPlayers() {
        return gamePlayers;
    }

    public void setPlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Game getGamestatus() {
        return gamestatus;
    }

    public void setGamestatus(Game gamestatus) {
        this.gamestatus = gamestatus;
    }
}