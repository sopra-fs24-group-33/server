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

    public GameLobby createGameLobby(Guest admin) {
        Player adminPlayer = new Player();
        adminPlayer.setName(admin.getGuestname());
        adminPlayer.setId(admin.getId());
        GameLobby gamelobby = new GameLobby();
        gamelobby.setAdmin(adminPlayer.getId());
        gamelobby.addPlayer(adminPlayer);
        gamelobby = gamelobbyRepository.save(gamelobby);
        gamelobbyRepository.flush();

        log.debug("Created Information for GameLobby: {}", gamelobby);
        return gamelobby;
    }
    
    public GameLobby addPlayer(Guest guest, GameLobby lobby) {
        Player player = new Player();
        player.setName(guest.getGuestname());
        player.setId(guest.getId());
        lobby.addPlayer(player);
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
    public GameLobby removePlayer(Guest guest, GameLobby lobby)  {
        Player player = new Player();
        player.setName(guest.getGuestname());
        player.setId(guest.getId());
        lobby.removePlayer(player);
        if (player.getId() == lobby.getAdmin()) {
            gamelobbyRepository.delete(lobby);
            gamelobbyRepository.flush();
            return null;
        }
        gamelobbyRepository.save(lobby);
        gamelobbyRepository.flush();
        return lobby;
    }

        
}