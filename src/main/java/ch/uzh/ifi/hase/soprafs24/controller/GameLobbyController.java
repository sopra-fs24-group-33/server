package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.service.PlayerService;
import org.springframework.web.bind.annotation.PathVariable;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameLobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameLobbyController {

    private final GameLobbyService gamelobbyService;
    private final PlayerService playerService;

    GameLobbyController(GameLobbyService gamelobbyService, PlayerService playerService) {
        this.gamelobbyService = gamelobbyService;
        this.playerService = playerService;
    }

    @GetMapping("/gamelobbys/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameLobbyGetDTO getGameLobby(@PathVariable Long id) {
        GameLobby gamelobby = gamelobbyService.getGameLobby(id);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(gamelobby);
    }

    @PostMapping("/gamelobbys")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameLobbyGetDTO createGameLobby(@RequestBody PlayerPostDTO playerPostDTO) {
        Player admin = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        GameLobby createdGameLobby = gamelobbyService.createGameLobby(admin);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(createdGameLobby);
    }

    @PostMapping("/gamelobbys/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameLobbyGetDTO addPlayer(@PathVariable Long id, @RequestBody PlayerPostDTO playerPostDTO) {
        Player player = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        GameLobby lobby = gamelobbyService.getLobby(id);
        GameLobby createdGameLobby = gamelobbyService.addPlayer(player, lobby);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(createdGameLobby);
    }
    @PutMapping("/gamelobbys/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameLobbyGetDTO removePlayer(@PathVariable Long id, @RequestBody PlayerPostDTO playerPostDTO) {
        Player player = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        GameLobby lobby = gamelobbyService.getLobby(id);
        lobby = gamelobbyService.removePlayer(player, lobby);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(lobby);
    }
}