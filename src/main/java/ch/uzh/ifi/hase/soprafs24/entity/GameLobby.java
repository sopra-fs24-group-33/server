package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
public class GameLobby {

  private List<Player> players = new ArrayList<>();

  private Game gamestatus = new Game();

  private Player admin = new Player();

  private String pin = new String();

  private String lobbyName = new String();

  private Long id;

  public Game startGame() {
    this.gamestatus.setPlayers(this.getPlayers());
    this.gamestatus.setLevel(1);
    this.gamestatus.setSuccessfulMove(0);
    this.gamestatus.setCurrentCard(0);
    this.gamestatus.updateGamestatus(this.gamestatus.getCurrentCard());
    return gamestatus;
  }

  public void addPlayer(Player player) {
    Player newPlayer = new Player();
    newPlayer.setUsername(player.getUsername());
    List<Player> newPlayers = this.getPlayers();
    newPlayers.add(newPlayer);
    this.players = newPlayers;
  }

  public void removePlayer(Player player) {

  }

  public List<Player> getPlayers() {
    return this.players;
  }

  public void setPlayers(List<Player> playerz) {
    this.players = playerz;
  }

  public void closeLobby() {

  }

  public String getPin() {
    return this.pin;
  }

  public void setPin(String pinString) {
    this.pin = pinString;
  }

  public String getLobbyName() {
    return this.lobbyName;
  }

  public void setLobbyName(String lobbyNameString) {
    this.lobbyName = lobbyNameString;
  }

  public Player getAdmin() {
    return this.admin;
  }

  public void setAdmin(Player newAdmin) {
    this.admin = newAdmin;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long idLong) {
    this.id = idLong;
  }

  public Game getGamestatus() {
    return this.gamestatus;
  }

  public void setGamestatus(Game newStatus) {
    this.gamestatus = newStatus;
  }
}
