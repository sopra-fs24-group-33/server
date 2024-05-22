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
import java.util.Random;

@Service
@Transactional
public class GameLobbyService {

    private final Logger log = LoggerFactory.getLogger(GameLobbyService.class);

    private final GameLobbyRepository gamelobbyRepository;

    private final Random random = new Random();

    @Autowired
    public GameLobbyService(@Qualifier("gamelobbyRepository") GameLobbyRepository gamelobbyRepository) {
        this.gamelobbyRepository = gamelobbyRepository;
    }

    public GameLobby getGameLobby(int gamePin) {
        GameLobby optionalGameLobby = gamelobbyRepository.findByPin(gamePin);
        if (optionalGameLobby != null) {
            return optionalGameLobby;
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GameLobby not found with Pin: " + gamePin);
        }
    }

	// adds the gameId to the lobby so that every other client (in the lobby) knows that the game started
    public void addGameId(int gamePin, Long gameId) {
        GameLobby gameLobby = getGameLobby(gamePin);
        gameLobby.setGameid(gameId);
        gamelobbyRepository.save(gameLobby);
        gamelobbyRepository.flush();
    }

    public GameLobby createGameLobby(Player admin) {
        GamePlayer adminGamePlayer = new GamePlayer();
        adminGamePlayer.setName(admin.getName());
        adminGamePlayer.setId(admin.getId());
        GameLobby gamelobby = new GameLobby();
        gamelobby.setPin(generatePin());
        gamelobby.setAdmin(adminGamePlayer.getId());
        gamelobby.addPlayer(adminGamePlayer);
        gamelobbyRepository.save(gamelobby);
        gamelobbyRepository.flush();

        log.debug("Created Information for GameLobby: {}", gamelobby);
        return gamelobby;
    }

    public void deleteReference(int gamePin)    {
        try {
            GameLobby lobby = getGameLobby(gamePin);
            lobby.setGameid(null);
            gamelobbyRepository.save(lobby);
            gamelobbyRepository.flush();
        } catch (ResponseStatusException ex) {
            throw ex;
        }
    }
    
    public GameLobby addPlayer(Player player, GameLobby lobby) {
        try {
            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.setName(player.getName());
            gamePlayer.setId(player.getId());
            lobby.addPlayer(gamePlayer);
            gamelobbyRepository.save(lobby);
            gamelobbyRepository.flush();
            return lobby;
        }   catch (ResponseStatusException ex) {
            throw ex;
        }
    }
    public GameLobby removePlayer(Player player, GameLobby lobby)  {
        try {
            Long id = player.getId();
            lobby.removePlayer(id);
            if (id == lobby.getAdmin()) {
                gamelobbyRepository.delete(lobby);
                gamelobbyRepository.flush();
                return null;
            }
            gamelobbyRepository.save(lobby);
            gamelobbyRepository.flush();
            return lobby;
        }
        catch (ResponseStatusException ex) {
            throw ex;
        }
    }
    private int generatePin() {
        int pin;

        do {
            pin = 100000 + random.nextInt(900000);
        } while (gamelobbyRepository.findByPin(pin) != null);

        return pin;
    }
}