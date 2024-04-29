package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerPostDTO;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameLobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.PlayerService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameLobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameLobbyController.class)
public class GameLobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameLobbyService gamelobbyService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private GameService gameService;


    @Test
    public void getGameLobby_invalidId_gamelobbyNotFound() throws Exception {

        int gamePin = 111111;
        given(gamelobbyService.getGameLobby(Mockito.anyInt())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "GameLobby not found"));

        MockHttpServletRequestBuilder getRequest = get("/gamelobbies/{gamePin}", gamePin)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void getGameLobby_validId_gamelobbyFound() throws Exception {

        GameLobby lobby = new GameLobby();
        int gamePin = 111111;
        lobby.setPin(gamePin);
        given(gamelobbyService.getGameLobby(Mockito.anyInt())).willReturn(lobby);

        MockHttpServletRequestBuilder getRequest = get("/gamelobbies/{gamePin}", gamePin)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void createGameLobby_validInput_gamelobbyCreated() throws Exception {
        // given
        Player admin = new Player();
        admin.setId(1L);
        GameLobby gamelobby = new GameLobby();
        gamelobby.setAdmin(admin.getId());
        gamelobby.setPin(111111);

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();

        given(gamelobbyService.createGameLobby(Mockito.any())).willReturn(gamelobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/gamelobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void createGameLobby_invalidInput_AdminNotFound() throws Exception {
        // given
        Player admin = new Player();
        admin.setId(1L);

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();

        given(gamelobbyService.createGameLobby(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "GameLobby already exists"));

        MockHttpServletRequestBuilder postRequest = post("/gamelobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void createGame_validInput_gameCreated() throws Exception {
        // given
        Player admin = new Player();
        int gamePin = 111111;
        admin.setId(1L);
        GameLobby gamelobby = new GameLobby();
        gamelobby.setAdmin(admin.getId());
        gamelobby.setPin(gamePin);
        Game game = new Game();
        game.setId(1L);

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();

        given(gameService.startGame(Mockito.any())).willReturn(game);
        given(gameService.updateGamestatus(Mockito.anyLong(), Mockito.anyInt())).willReturn(game);
        Mockito.doNothing().when(gamelobbyService).addGameId(Mockito.anyInt(), Mockito.anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/startgame/{gamePin}", gamePin)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void createGame_invalidInput_LobbyNotFound() throws Exception {
        // given
        Player admin = new Player();
        admin.setId(1L);
        int gamePin = 111111;
        GameLobby gamelobby = new GameLobby();
        gamelobby.setPin(gamePin);

        given(gameService.startGame(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "GameLobby already exists"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/startgame/{gamePin}", gamePin)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void addPlayer_validInput_playerAdded() throws Exception {
        // given
        Player admin = new Player();
        int gamePin = 111111;
        admin.setId(1L);
        GameLobby gamelobby = new GameLobby();
        gamelobby.setAdmin(admin.getId());
        gamelobby.setPin(gamePin);

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();

        given(gamelobbyService.getGameLobby(Mockito.anyInt())).willReturn(gamelobby);
        given(gamelobbyService.addPlayer(Mockito.any(), Mockito.any())).willReturn(gamelobby);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/gamelobbies/{gamePin}", gamePin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void addPlayer_invalidInput_LobbyNotFound() throws Exception {
        // given
        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        int gamePin = 111111;
        GameLobby gamelobby = new GameLobby();
        gamelobby.setPin(gamePin);

        given(gamelobbyService.getGameLobby(Mockito.anyInt())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "GameLobby already exists"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/gamelobbies/{gamePin}", gamePin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void removePlayer_validInput_playerRemoved() throws Exception {
        // given
        Player admin = new Player();
        int gamePin = 111111;
        admin.setId(1L);
        GameLobby gamelobby = new GameLobby();
        gamelobby.setAdmin(admin.getId());
        gamelobby.setPin(gamePin);

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();

        given(gamelobbyService.getGameLobby(Mockito.anyInt())).willReturn(gamelobby);
        given(gamelobbyService.removePlayer(Mockito.any(), Mockito.any())).willReturn(gamelobby);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/gamelobbies/{gamePin}", gamePin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void removePlayer_invalidInput_LobbyNotFound() throws Exception {
        // given
        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        int gamePin = 111111;
        GameLobby gamelobby = new GameLobby();
        gamelobby.setPin(gamePin);

        given(gamelobbyService.getGameLobby(Mockito.anyInt())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "GameLobby already exists"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/gamelobbies/{gamePin}", gamePin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }


    /**
     * Helper Method to convert gamelobbyPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test GameLobby", "name": "testName"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}