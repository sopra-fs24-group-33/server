package ch.uzh.ifi.hase.soprafs24.rest.mapper;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOGameMapperTest {

    @Test
    public void testCreateGame_fromGamePostDTO_toGame_success() {
        // create GamePostDTO
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setCurrentCard(1);
        gamePostDTO.setLevel(1);
        gamePostDTO.setSuccessfulMove(0);
        gamePostDTO.setId(1L);

        List<Integer> cardStack = Arrays.asList(1, 2, 3);
        gamePostDTO.setCardStack(cardStack);

        List<GamePlayer> players = new ArrayList<>();

        gamePostDTO.setPlayers(players);


        Game game = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);


        assertEquals(gamePostDTO.getCurrentCard(), game.getCurrentCard());
        assertEquals(gamePostDTO.getLevel(), game.getLevel());
        assertEquals(gamePostDTO.getSuccessfulMove(), game.getSuccessfulMove());
        assertEquals(gamePostDTO.getId(), game.getId());
        Set<Integer> expectedCardStack = new HashSet<>(gamePostDTO.getCardStack());
        assertEquals(expectedCardStack, game.getCardStack());
        assertEquals(gamePostDTO.getPlayers().size(), game.getPlayers().size());
    }

    @Test
    public void testGetGame_fromGame_toGameGetDTO_success() {
        // create Game
        Game game = new Game();
        game.setCurrentCard(1);
        game.setLevel(1);
        game.setSuccessfulMove(0);
        game.setId(1L);

        Set<Integer> cardStack = new HashSet<>(Arrays.asList(1, 2, 3));
        game.setCardStack(cardStack);

        Set<GamePlayer> players = new HashSet<>();
        game.setPlayers(players);


        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);


        assertEquals(game.getCurrentCard(), gameGetDTO.getCurrentCard());
        assertEquals(game.getLevel(), gameGetDTO.getLevel());
        assertEquals(game.getSuccessfulMove(), gameGetDTO.getSuccessfulMove());
        assertEquals(game.getId(), gameGetDTO.getId());
        List<Integer> expectedCardStack = new ArrayList<>(game.getCardStack());
        assertEquals(expectedCardStack, gameGetDTO.getCardStack());
        assertEquals(game.getPlayers().size(), gameGetDTO.getPlayers().size());
    }
}
