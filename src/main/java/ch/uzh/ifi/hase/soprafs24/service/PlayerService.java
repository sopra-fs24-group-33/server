package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

import java.util.List;
import java.util.UUID;




/**
 * Guest Service
 * This class is the "worker" and responsible for all functionality related to
 * the guest
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    @Autowired
    public PlayerService(@Qualifier("guestRepository") PlayerRepository playerRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    public List<Player> getPlayers() {
        return this.playerRepository.findAll();
    }

    public Player getPlayer(Long guestId) {
        Optional<Player> optionalGuest = playerRepository.findById(guestId);
        if (optionalGuest.isPresent()) {
            return optionalGuest.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found with ID: " + guestId);
        }
    }

    public Player logoutUser(Long id)   {
        Optional<Player> guest = playerRepository.findById(id);
        if (guest.isPresent())  {
            Player foundPlayer = guest.get();
            playerRepository.delete(foundPlayer);
            playerRepository.flush();
            return foundPlayer;
        }
        else{
            return null;
        }
    }

    public Player loginPlayer()   {
        Player player = new Player();
        player.setToken(UUID.randomUUID().toString());
        player.setStatus(UserStatus.OFFLINE);
        playerRepository.save(player);
        playerRepository.flush();
        return player;
    }
    public Player loginUser(User loginUser)   {
        User user = userRepository.findByUsername(loginUser.getUsername());
        if (user != null && loginUser.getPassword().equals(user.getPassword())) {
            Player loginPlayer = new Player(user);
            playerRepository.save(loginPlayer);
            playerRepository.flush();
            return loginPlayer;
        }
        else{
            throw new RuntimeException();
        }
    }



    public Player createPlayer(Player newPlayer) {
        newPlayer.setToken(UUID.randomUUID().toString());
        newPlayer.setStatus(UserStatus.OFFLINE);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newPlayer = playerRepository.save(newPlayer);
        playerRepository.flush();

        log.debug("Created Information for Guest: {}", newPlayer);
        return newPlayer;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * guestname and the name
     * defined in the Guest entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param guestToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see Player
     */
}

