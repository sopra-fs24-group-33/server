package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.PlayerService;
import org.springframework.web.bind.annotation.PathVariable;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;
    private final PlayerService playerService;

    UserController(UserService userService, PlayerService playerService) {
        this.userService = userService; this.playerService = playerService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }


    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PlayerGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User createdUser = userService.createUser(userInput);
        Player newPlayer = playerService.loginUser(createdUser);
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(newPlayer);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PlayerGetDTO loginUser(@RequestBody UserPostDTO userPostDTO)  {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        Player logged_in_player = playerService.loginUser(userInput);
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(logged_in_player);
    }
}
