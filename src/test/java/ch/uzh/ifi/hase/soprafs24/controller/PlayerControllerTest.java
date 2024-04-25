package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerPostDTO;
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

@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Test
    public void givenPlayers_whenGetPlayers_thenReturnJsonArray() throws Exception {
        // given
        Player player = new Player();
        player.setName("firstname@lastname");

        List<Player> allPlayers = Collections.singletonList(player);

        // this mocks the PlayerService -> we define above what the playerService should
        // return when getPlayers() is called
        given(playerService.getPlayers()).willReturn(allPlayers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/players").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(player.getName())));
    }

    @Test
    public void createPlayer_validInput_playerCreated() throws Exception {
        // given
        Player player = new Player();
        player.setId(1L);
        player.setName("testName");
        player.setToken("1");

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setName("testName");

        given(playerService.createPlayer(Mockito.any())).willReturn(player);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(player.getId().intValue())))
                .andExpect(jsonPath("$.name", is(player.getName())));
    }

    @Test
    public void createPlayer_invalidInput_playerExists() throws Exception {
        // given
        Player player = new Player();
        player.setName("testName");

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setName("testName");

        given(playerService.createPlayer(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Player already exists"));

        MockHttpServletRequestBuilder postRequest = post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void putPlayer_validInput_playerUpdated() throws Exception {
        // given
        Player player = new Player();
        player.setId(1L);
        Long id = player.getId();
        player.setName("testName");
        player.setToken("1");

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setName("testName");




        MockHttpServletRequestBuilder putRequest = put("/players/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        // Then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());

    }

    @Test
    public void getPlayer_validId_playerFound() throws Exception {
        Player player = new Player();
        player.setId(1L);
        Long id = player.getId();
        player.setName("testName");
        player.setToken("1");
        given(playerService.getPlayer(Mockito.any())).willReturn(player);
        MockHttpServletRequestBuilder getRequest = get("/players/{id}", id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(player.getId().intValue())))
                .andExpect(jsonPath("$.name", is(player.getName())));
    }

    @Test
    public void getPlayer_invalidId_playerNotFound() throws Exception {

        Long id = 1L;
        given(playerService.getPlayer(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

        MockHttpServletRequestBuilder getRequest = get("/players/{id}", id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void putPlayer_invalidId_playerNotFound() throws Exception {

        Player player = new Player();
        player.setId(1L);
        Long id = player.getId();
        player.setName("testName");
        player.setToken("1");

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setName("testName");

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"))
                .when(playerService);

        MockHttpServletRequestBuilder putRequest = put("/players/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }


    /**
     * Helper Method to convert playerPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test Player", "name": "testName"}
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
