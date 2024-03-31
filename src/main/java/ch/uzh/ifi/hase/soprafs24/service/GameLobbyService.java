package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.GameLobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class GameLobbyService {

    private final Logger log = LoggerFactory.getLogger(GameLobbyService.class);

    private final GameLobbyRepository gamelobbyRepository;

    @Autowired
    public GameLobbyService(@Qualifier("gamelobbyRepository") GameLobbyRepository gamelobbyRepository) {
        this.gamelobbyRepository = gamelobbyRepository;
    }

    public GameLobby getGameLobby(Long gamelobbyId) {
        Optional<GameLobby> optionalGameLobby = gamelobbyRepository.findById(gamelobbyId);
        if (optionalGameLobby.isPresent()) {
            return optionalGameLobby.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GameLobby not found with ID: " + gamelobbyId);
        }
    }

    public GameLobby createGameLobby(Player admin) {
        GamePlayer adminGamePlayer = new GamePlayer();
        adminGamePlayer.setName(admin.getGuestname());
        adminGamePlayer.setId(admin.getId());
        GameLobby gamelobby = new GameLobby();
        gamelobby.setAdmin(adminGamePlayer.getId());
        gamelobby.addPlayer(adminGamePlayer);
        gamelobby = gamelobbyRepository.save(gamelobby);
        gamelobbyRepository.flush();

        log.debug("Created Information for GameLobby: {}", gamelobby);
        return gamelobby;
    }
    
    public GameLobby addPlayer(Player player, GameLobby lobby) {
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setName(player.getGuestname());
        gamePlayer.setId(player.getId());
        lobby.addPlayer(gamePlayer);
        gamelobbyRepository.save(lobby);
        gamelobbyRepository.flush();
        return lobby;
    }
    public GameLobby getLobby(Long id)  {
        Optional<GameLobby> optionalGameLobby = gamelobbyRepository.findById(id);
        if (optionalGameLobby.isPresent()) {
            return optionalGameLobby.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    public GameLobby removePlayer(Player player, GameLobby lobby)  {
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setName(player.getGuestname());
        gamePlayer.setId(player.getId());
        lobby.removePlayer(gamePlayer);
        if (gamePlayer.getId() == lobby.getAdmin()) {
            gamelobbyRepository.delete(lobby);
            gamelobbyRepository.flush();
            return null;
        }
        gamelobbyRepository.save(lobby);
        gamelobbyRepository.flush();
        return lobby;
    }
}