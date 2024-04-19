package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GAMELOBBY")
public class GameLobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    private Long admin;

    @Id
    private int pin;

    @Column
    private Long gameid;

    @OneToMany(mappedBy = "gameLobby", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GamePlayer> gamePlayers = new ArrayList<>();

    public void addPlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
        gamePlayer.setGameLobby(this);
    }

    public void removePlayer(Long id) {
        for (GamePlayer player : this.getPlayers())  {
            if (player.getId() == id)   {
                gamePlayers.remove(player);
                player.setGameLobby(null);
                break;
            }
        }
    }




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

    public Long getGameId() {
        return gameid;
    }

    public void setGameId(Long gameid) {
        this.gameid = gameid;
    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
}
