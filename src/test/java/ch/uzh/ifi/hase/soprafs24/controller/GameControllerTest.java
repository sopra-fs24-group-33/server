package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameLobbyService;
import ch.uzh.ifi.hase.soprafs24.service.PlayerService;
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

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private GameLobbyService gamelobbyService;

    @Test
    public void getGame_invalidId_gameNotFound() throws Exception {

        Long id = 1L;
        given(gameService.getGame(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        MockHttpServletRequestBuilder getRequest = get("/game/{id}", id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void getGame_validId_gameFound() throws Exception {

        Game game = new Game();
        Long id = 1L;
        game.setId(id);
        given(gameService.getGame(Mockito.any())).willReturn(game);

        MockHttpServletRequestBuilder getRequest = get("/game/{id}", id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(game.getId().intValue())));
    }

    @Test
    public void playCard_validId_gameFound() throws Exception {
        Game game = new Game();
        Long id = 1L;
        int playedCard = 0;
        game.setId(id);
        given(gameService.getGame(Mockito.any())).willReturn(game);

        MockHttpServletRequestBuilder putRequest = put("/move/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playedCard));

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void playCard_invalidId_gameNotFound() throws Exception {
        Game game = new Game();
        Long id = 1L;
        int playedCard = 0;
        game.setId(id);
        given(gameService.updateGamestatus(Mockito.anyLong(), Mockito.anyInt())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        MockHttpServletRequestBuilder putRequest = put("/move/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playedCard));

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteGame_validId_gameFound() throws Exception {

        Long id = 1L;
        Mockito.doNothing().when(gameService).deleteGame(Mockito.anyLong());



        MockHttpServletRequestBuilder deleteRequest = delete("/endgame/{id}", id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteGame_invalidId_gameNotFound() throws Exception {
        Long id = 1L;

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found")).when(gameService).deleteGame(Mockito.anyLong());


        MockHttpServletRequestBuilder deleteRequest = delete("/endgame/{id}", id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound());
    }


    /**
     * Helper Method to convert gamePostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test Game", "name": "testName"}
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