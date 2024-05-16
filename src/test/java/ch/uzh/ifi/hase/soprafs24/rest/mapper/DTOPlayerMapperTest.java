package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerPostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOPlayerMapperTest {

    @Test
    public void testCreatePlayer_fromPlayerPostDTO_toPlayer_success() {
        // create PlayerPostDTO
        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setName("playerName");
        playerPostDTO.setShame_tokens(0);

        Player player = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);

        assertEquals(playerPostDTO.getName(), player.getName());
        assertEquals(playerPostDTO.getShame_tokens(), player.getShame_tokens());
    }

    @Test
    public void testGetPlayer_fromPlayer_toPlayerGetDTO_success() {

        Player player = new Player();
        player.setName("playerName");
        player.setShame_tokens(0);
        player.setToken("1");

        PlayerGetDTO playerGetDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);

        assertEquals(player.getId(), playerGetDTO.getId());
        assertEquals(player.getName(), playerGetDTO.getName());
        assertEquals(player.getShame_tokens(), playerGetDTO.getShame_tokens());
    }
}
