package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameLobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.Arrays;


@Service
@Transactional
public class GameLobbyService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final GameLobbyRepository gameLobbyRepository;

  @Autowired
  public GameLobbyService(@Qualifier("GameLobbyRepository") GameLobbyRepository lobbyRepository) {
    this.gameLobbyRepository = lobbyRepository;
  }

  public List<Player> getPlayers(int lobbyId) {
    Game myGame = gameLobbyRepository.findById(lobbyId);
    return Arrays.asList(myGame.getPlayers());
  }

  public Player addPlayer(Player newPlayer) {
    
  }

  public GameLobby findGameLobbybyId(Long id) {
    if (GameLobbyRepository.existsById(id)) {
      Optional<GameLobby> optionalGameLobby = GameLobbyRepository.findById(id);
      return optionalGameLobby.get();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND); 
    }
  }

  public Game updateGameStatus(Long lobbyId, int playedCard) {
    GameLobby myGameLobby = GameLobbyService.findGameLobbyById(lobbyId);
    Game updatedGamestatus = myGameLobby.getGamestatus();
    myGameLobby.setGamestatus(updatedGamestatus.updateGamestatus(playedCard));
  }
}
