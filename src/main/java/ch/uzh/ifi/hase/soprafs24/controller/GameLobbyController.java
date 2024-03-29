package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.service.GuestService;
import org.springframework.web.bind.annotation.PathVariable;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameLobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
public class GameLobbyController {

    private final GameLobbyService gamelobbyService;
    private final GuestService guestService;

    GameLobbyController(GameLobbyService gamelobbyService, GuestService guestService) {
        this.gamelobbyService = gamelobbyService;
        this.guestService = guestService;
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
    public GameLobbyGetDTO createGameLobby(@RequestBody GuestPostDTO guestPostDTO) {
        Guest admin = DTOMapper.INSTANCE.convertGuestPostDTOtoEntity(guestPostDTO);
        GameLobby createdGameLobby = gamelobbyService.createGameLobby(admin);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(createdGameLobby);
    }

    @PostMapping("/gamelobbys/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameLobbyGetDTO addPlayer(@PathVariable Long id, @RequestBody GuestPostDTO guestPostDTO) {
        Guest player = DTOMapper.INSTANCE.convertGuestPostDTOtoEntity(guestPostDTO);
        GameLobby lobby = gamelobbyService.getLobby(id);
        GameLobby createdGameLobby = gamelobbyService.addPlayer(player, lobby);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(createdGameLobby);
    }
    @PutMapping("/gamelobbys/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameLobbyGetDTO removePlayer(@PathVariable Long id, @RequestBody GuestPostDTO guestPostDTO) {
        Guest player = DTOMapper.INSTANCE.convertGuestPostDTOtoEntity(guestPostDTO);
        GameLobby lobby = gamelobbyService.getLobby(id);
        lobby = gamelobbyService.removePlayer(player, lobby);
        return DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(lobby);
    }
}