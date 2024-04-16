package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game getGame(Long id) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame != null) {
            return optionalGame.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with Pin: " + id);
        }
    }

    public Game startGame(GameLobby lobby)  {
        Game game = new Game();
        game.startGame(lobby);
        gameRepository.save(game);
        gameRepository.flush();
        return game;
    }

    public Game updateGamestatus(Long id, Integer playedCard)   {
        Game game = getGame(id);
        game = game.updateGamestatus(playedCard);
        gameRepository.save(game);
        gameRepository.flush();
        return game;
    }
}
