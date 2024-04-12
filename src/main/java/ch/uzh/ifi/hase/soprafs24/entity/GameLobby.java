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

    @OneToOne
    private Game gamestatus;

    @OneToMany(mappedBy = "gameLobby", cascade = CascadeType.ALL)
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

    public Game startGame() {
        this.gamestatus.setPlayers(this.getPlayers());
        this.gamestatus.setLevel(1);
        this.gamestatus.setSuccessfulMove(0);
        this.gamestatus.setCurrentCard(0);
        this.gamestatus.updateGamestatus(this.gamestatus.getCurrentCard());
        return gamestatus;
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
