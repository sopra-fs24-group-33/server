package ch.uzh.ifi.hase.soprafs24.rest.mapper;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import javax.persistence.Column;
import javax.persistence.Id;

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

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "gamesPlayed", target = "gamesPlayed")
    @Mapping(source = "shame_tokens", target = "shame_tokens")
    @Mapping(source = "current_shame_tokens", target = "current_shame_tokens")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "token", target = "token")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "gamesPlayed", target = "gamesPlayed")
    @Mapping(source = "shame_tokens", target = "shame_tokens")
    @Mapping(source = "current_shame_tokens", target = "current_shame_tokens")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "token", target = "token")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "guestname", target = "guestname")
    @Mapping(source = "shame_tokens", target = "shame_tokens")
    @Mapping(source = "isUser", target = "isUser")
    Guest convertGuestPostDTOtoEntity(GuestPostDTO guestPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "guestname", target = "guestname")
    @Mapping(source = "shame_tokens", target = "shame_tokens")
    @Mapping(source = "isUser", target = "isUser")
    GuestGetDTO convertEntityToGuestGetDTO(Guest guest);
}
