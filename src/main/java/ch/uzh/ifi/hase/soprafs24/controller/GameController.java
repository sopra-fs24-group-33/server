package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.springframework.web.bind.annotation.PathVariable;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    private final GameLobbyService gamelobbyService;
    private final PlayerService playerService;
    private final GameService gameService;

    GameController(GameLobbyService gamelobbyService, PlayerService playerService, GameService gameService) {
        this.gamelobbyService = gamelobbyService;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @GetMapping("/game/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable Long id) {
        Game game = gameService.getGame(id);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }
    @PutMapping("/move/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO playCard(@PathVariable Long id, @RequestBody Integer playedCard) {
        Game updatedLobby = gameService.updateGamestatus(id, playedCard);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(updatedLobby);
    }

    @DeleteMapping("/endgame/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void playCard(@PathVariable Long id) {
        gameService.deleteGame(id);
    }
}
