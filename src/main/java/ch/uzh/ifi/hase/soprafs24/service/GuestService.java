package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Guest;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GuestRepository;
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
public class GuestService {

    private final Logger log = LoggerFactory.getLogger(GuestService.class);

    private final GuestRepository guestRepository;
    private final UserRepository userRepository;

    @Autowired
    public GuestService(@Qualifier("guestRepository") GuestRepository guestRepository, UserRepository userRepository) {
        this.guestRepository = guestRepository;
        this.userRepository = userRepository;
    }

    public List<Guest> getGuests() {
        return this.guestRepository.findAll();
    }

    public Guest getGuest(Long guestId) {
        Optional<Guest> optionalGuest = guestRepository.findById(guestId);
        if (optionalGuest.isPresent()) {
            return optionalGuest.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found with ID: " + guestId);
        }
    }

    public Guest logoutUser(Long id)   {
        Optional<Guest> guest = guestRepository.findById(id);
        if (guest.isPresent())  {
            Guest foundGuest = guest.get();
            guestRepository.delete(foundGuest);
            guestRepository.flush();
            return foundGuest;
        }
        else{
            return null;
        }
    }

    public Guest loginGuest()   {
        Guest guest = new Guest();
        guest.setToken(UUID.randomUUID().toString());
        guest.setStatus(UserStatus.OFFLINE);
        guestRepository.save(guest);
        guestRepository.flush();
        return guest;
    }
    public Guest loginUser(Long id)   {
        Optional<User> loginUser = userRepository.findById(id);
        if (loginUser.isPresent())  {
            User user = loginUser.get();
            Guest loginGuest = new Guest(user);
            guestRepository.save(loginGuest);
            guestRepository.flush();
            return loginGuest;
        }
        else{
            throw new RuntimeException();
        }
    }



    public Guest createGuest(Guest newGuest) {
        newGuest.setToken(UUID.randomUUID().toString());
        newGuest.setStatus(UserStatus.OFFLINE);

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newGuest = guestRepository.save(newGuest);
        guestRepository.flush();

        log.debug("Created Information for Guest: {}", newGuest);
        return newGuest;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * guestname and the name
     * defined in the Guest entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param guestToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see Guest
     */
}

