package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.service.AgoraService;
import ch.uzh.ifi.hase.soprafs24.service.PlayerService;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgoraController.class)
public class AgoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgoraService agoraService;

    @MockBean
    PlayerService playerService;

    @Test
    public void givenAgoras_whenGetAgoras_throw_exception() throws Exception {
        // given
        Long playerId = 1L;
        int gamePin = 111111;
        given(agoraService.createToken(Mockito.anyInt(), Mockito.anyLong())).willReturn("testToken");
        // when
        MockHttpServletRequestBuilder getRequest = get("/agoratoken/{gamePin}/{playerId}", gamePin, playerId).contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isCreated());
    }

    @Test
    public void givenAgoras_whenGetAgoras_thenReturnJsonArray() throws Exception {
        // given
        Long playerId = 1L;
        int gamePin = 111111;
        given(agoraService.createToken(Mockito.anyInt(), Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        // when
        MockHttpServletRequestBuilder getRequest = get("/agoratoken/{gamePin}/{playerId}", gamePin, playerId).contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

}
