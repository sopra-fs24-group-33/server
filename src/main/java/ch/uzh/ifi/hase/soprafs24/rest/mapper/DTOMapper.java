package ch.uzh.ifi.hase.soprafs24.rest.mapper;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    UserGetDTO convertEntityToUserGetDTO(User user);


    Player convertPlayerPostDTOtoEntity(PlayerPostDTO playerPostDTO);


    PlayerGetDTO convertEntityToPlayerGetDTO(Player player);

    GameLobby convertGameLobbyPostDTOtoEntity(GameLobbyPostDTO gamelobbyPostDTO);

    GameLobbyGetDTO convertEntityToGameLobbyGetDTO(GameLobby gamelobby);

    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

    GameGetDTO convertEntityToGameGetDTO(Game game);



}
