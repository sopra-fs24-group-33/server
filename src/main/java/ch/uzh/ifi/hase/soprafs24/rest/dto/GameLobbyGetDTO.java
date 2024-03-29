package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameLobbyGetDTO {
        private Long id;

        private Long admin;

        private String pin;

        private List<Player> players = new ArrayList<>();

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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Game getGamestatus() {
        return gamestatus;
    }

    public void setGamestatus(Game gamestatus) {
        this.gamestatus = gamestatus;
    }
}
