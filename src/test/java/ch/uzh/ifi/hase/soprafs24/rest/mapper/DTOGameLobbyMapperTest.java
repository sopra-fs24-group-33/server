package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameLobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameLobbyPostDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOGameLobbyMapperTest {

    @Test
    public void testCreateGameLobby_fromGameLobbyPostDTO_toGameLobby_success() {

        GameLobbyPostDTO gameLobbyPostDTO = new GameLobbyPostDTO();
        gameLobbyPostDTO.setAdmin(1L);
        gameLobbyPostDTO.setPin(1234);
        gameLobbyPostDTO.setGameid(1L);

        List<GamePlayer> players = new ArrayList<>();

        gameLobbyPostDTO.setPlayers(players);


        GameLobby gameLobby = DTOMapper.INSTANCE.convertGameLobbyPostDTOtoEntity(gameLobbyPostDTO);


        assertEquals(gameLobbyPostDTO.getAdmin(), gameLobby.getAdmin());
        assertEquals(gameLobbyPostDTO.getPin(), gameLobby.getPin());
        assertEquals(gameLobbyPostDTO.getGameid(), gameLobby.getGameid());
        assertEquals(gameLobbyPostDTO.getPlayers().size(), gameLobby.getPlayers().size());
    }

    @Test
    public void testGetGameLobby_fromGameLobby_toGameLobbyGetDTO_success() {

        GameLobby gameLobby = new GameLobby();
        gameLobby.setAdmin(1L);
        gameLobby.setPin(1234);
        gameLobby.setGameid(1L);

        List<GamePlayer> players = new ArrayList<>();
        gameLobby.setPlayers(players);

        GameLobbyGetDTO gameLobbyGetDTO = DTOMapper.INSTANCE.convertEntityToGameLobbyGetDTO(gameLobby);

        assertEquals(gameLobby.getAdmin(), gameLobbyGetDTO.getAdmin());
        assertEquals(gameLobby.getPin(), gameLobbyGetDTO.getPin());
        assertEquals(gameLobby.getGameid(), gameLobbyGetDTO.getGameid());
        assertEquals(gameLobby.getPlayers().size(), gameLobbyGetDTO.getPlayers().size());
    }
}




