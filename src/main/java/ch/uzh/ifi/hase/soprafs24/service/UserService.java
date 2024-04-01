package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
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
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId);
        }
    }


    public User logoutUser(User logoutUser)   {
        logoutUser.setStatus(UserStatus.OFFLINE);
        logoutUser = userRepository.save(logoutUser);
        userRepository.flush();
        return logoutUser;
    }

    public void putUser(Long id, User requestBody) {
        Optional <User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            if (!(requestBody.getUsername().equals(updatedUser.getUsername()))) {
                checkIfUserExists(requestBody);
            }
            updatedUser.setUsername(requestBody.getUsername());
            updatedUser.setPassword(requestBody.getPassword());
            userRepository.save(updatedUser);
            userRepository.flush();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id);
        }
    }


    public User createUser(User newUser) {
        checkIfUserExists(newUser);
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        newUser = userRepository.save(newUser);
        userRepository.flush();
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**getuserby
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userToBeCreated.getUsername().isEmpty())    {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is empty");
        }
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
    }
}
