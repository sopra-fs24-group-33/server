package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class GameLobbyController {

  private final GameLobbyService gameLobbyService;

  private final UserService userService;

  GameLobbyController(GameLobbyService lobbyService) {
    this.gameLobbyService = lobbyService;
  }

  UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/lobbies")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public GameLobby createLobby(long userID) {
    User myUser = userRepository.findById(userID);
    
  }

  @PostMapping("/lobbies/{lobbyID}")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public GameLobby joinLobby(@PathVariable Long lobbyID, Player player, String gamePin) {
    
  }

  @PutMapping("/lobbies/{lobbyID}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void playCard(@PathVariable Long lobbyID, int playedCard){
    Game myGame = GameLobbyService.updateGameStatus(lobbyID, playedCard);
  }

  @DeleteMapping("/lobbies/{lobbyID}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void removeUserFromLobby(@PathVariable Long lobbyID, Player player){
    
  }

}
