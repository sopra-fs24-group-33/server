package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
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
    private final UserService userService;

    @Autowired
    public PlayerService(@Qualifier("guestRepository") PlayerRepository playerRepository, UserRepository userRepository, UserService userService) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Player> getPlayers() {
        return this.playerRepository.findAll();
    }

    public Player getPlayer(Long guestId) {
        Optional<Player> optionalGuest = playerRepository.findById(guestId);
        if (optionalGuest.isPresent()) {
            return optionalGuest.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found.");
        }
    }

    public Player logoutUser(Long id)   {
        try {
            Optional<Player> guest = playerRepository.findById(id);
            if (guest.isPresent()) {
                Player foundPlayer = guest.get();
                playerRepository.delete(foundPlayer);
                playerRepository.flush();
                return foundPlayer;
            }
            else {
                return null;
            }
        } catch (ResponseStatusException ex) {
            throw ex;
        }
    }

    public Player loginPlayer()   {
        Player player = new Player();
        player.setToken(UUID.randomUUID().toString());
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
    }

    public void addShame_token(Long id)    {
        try {
            Player player = getPlayer(id);
            player.setShame_tokens(player.getShame_tokens() + 1);
            playerRepository.save(player);
            playerRepository.flush();
            if (player.getIsUser() != null) {
                userService.addShame_token(player.getIsUser());
            }
        } catch (ResponseStatusException ex) {
            throw ex;
        }
    }

    public Player increaseGamesPlayed(Player player) {
        if (player.getIsUser() != null) {
            User myUser = userService.getUser(player.getIsUser());
            // System.out.println(myUser.getGamesPlayed());
            myUser.setGamesPlayed((myUser.getGamesPlayed() + 1));
            userRepository.save(myUser);
            userRepository.flush();
            // System.out.println(myUser.getGamesPlayed());
        }
        return player;
    }

    public Player increaseRoundsWon(Player player) {
        if (player.getIsUser() != null) {
            User myUser = userService.getUser(player.getIsUser());
            // System.out.println(myUser.getGamesPlayed());
            myUser.setRoundsWon((myUser.getRoundsWon() + 1));
            userRepository.save(myUser);
            userRepository.flush();
            // System.out.println(myUser.getGamesPlayed());
        }
        return player;
    }

    public Player increaseFlawlessWin(Player player) {
        if (player.getIsUser() != null) {
            User myUser = userService.getUser(player.getIsUser());
            // System.out.println(myUser.getGamesPlayed());
            myUser.setFlawlessWins((myUser.getFlawlessWins() + 1));
            userRepository.save(myUser);
            userRepository.flush();
            // System.out.println(myUser.getGamesPlayed());
        }
        return player;
    }

    public Player createPlayer(Player newPlayer) {
        newPlayer.setToken(UUID.randomUUID().toString());
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

