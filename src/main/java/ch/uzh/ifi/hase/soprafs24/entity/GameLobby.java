package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GAMELOBBY")
public class GameLobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long admin;

    @Column
    private String pin;

    @OneToOne
    private Game gamestatus;

    @OneToMany(mappedBy = "gameLobby", cascade = CascadeType.ALL)
    private List<GamePlayer> gamePlayers = new ArrayList<>();

    public void addPlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
        gamePlayer.setGameLobby(this);
    }

    public void removePlayer(GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer);
        gamePlayer.setGameLobby(null);
    }

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

    public Game getGamestatus() {
        return gamestatus;
    }

    public void setGamestatus(Game gamestatus) {
        this.gamestatus = gamestatus;
    }

    public List<GamePlayer> getPlayers() {
        return gamePlayers;
    }

    public void setPlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
}
