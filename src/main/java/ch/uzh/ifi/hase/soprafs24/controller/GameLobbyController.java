package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.springframework.web.bind.annotation.PathVariable;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameLobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameLobbyController {

    private final GameLobbyService gamelobbyService;
    private final GameService gameService;
    private final PlayerService playerService;

    GameLobbyController(GameLobbyService gamelobbyService, PlayerService playerService, GameService gameService) {
        this.gamelobbyService = gamelobbyService;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @GetMapping("/gamelobbies/{gamePin}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLobbyGetDTO getGameLobby(@PathVariable int gamePin) {
        GameLobby gamelobby = gamelobbyService.getGameLobby(gamePin);
			System.out.println("HAEEEEEEEEEEEEE:" + gamelobby.getGameId());
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(gamelobby);
    }

    @PostMapping("/gamelobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameLobbyGetDTO createGameLobby(@RequestBody PlayerPostDTO playerPostDTO) {
        Player admin = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        GameLobby createdGameLobby = gamelobbyService.createGameLobby(admin);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(createdGameLobby);
    }

    @PostMapping("/startgame/{gamePin}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@PathVariable int gamePin) {
        GameLobby lobby = gamelobbyService.getGameLobby(gamePin);
        Game game = gameService.startGame(lobby);
				gamelobbyService.addGameId(gamePin, game.getId());
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PostMapping("/gamelobbies/{gamePin}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameLobbyGetDTO addPlayer(@PathVariable int gamePin, @RequestBody PlayerPostDTO playerPostDTO) {
        Player player = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        GameLobby lobby = gamelobbyService.getGameLobby(gamePin);
        GameLobby createdGameLobby = gamelobbyService.addPlayer(player, lobby);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(createdGameLobby);
    }
    @PutMapping("/gamelobbies/{gamePin}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameLobbyGetDTO removePlayer(@PathVariable Integer gamePin, @RequestBody PlayerPostDTO playerPostDTO) {
        Player player = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        GameLobby lobby = gamelobbyService.getGameLobby(gamePin);
        lobby = gamelobbyService.removePlayer(player, lobby);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(lobby);
    }
}