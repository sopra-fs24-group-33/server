package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerGetDTO;
import org.springframework.web.bind.annotation.PathVariable;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Player Controller
 * This class is responsible for handling all REST request that are related to
 * the guest.
 * The controller will receive the request and delegate the execution to the
 * PlayerService and finally return the result.
 */
@RestController
public class PlayerController {

    private final PlayerService playerService;

    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/guests")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerGetDTO> getAllPlayers() {
        List<ch.uzh.ifi.hase.soprafs24.entity.Player> players = playerService.getPlayers();
        List<PlayerGetDTO> playerGetDTOS = new ArrayList<>();
        for (ch.uzh.ifi.hase.soprafs24.entity.Player player : players) {
            playerGetDTOS.add(DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player));
        }
        return playerGetDTOS;
    }

    @GetMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO getPlayer(@PathVariable Long id) {
        ch.uzh.ifi.hase.soprafs24.entity.Player player = playerService.getPlayer(id);
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);
    }

    @PostMapping("/guests")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PlayerGetDTO createPlayer() {
        ch.uzh.ifi.hase.soprafs24.entity.Player player = playerService.loginPlayer();
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);
    }
    @DeleteMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PlayerGetDTO logoutUser(@PathVariable Long id)  {
        ch.uzh.ifi.hase.soprafs24.entity.Player logged_in_player = playerService.logoutUser(id);
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(logged_in_player);
    }
}
