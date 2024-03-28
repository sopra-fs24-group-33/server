package ch.uzh.ifi.hase.soprafs24.controller;
import org.springframework.web.bind.annotation.PathVariable;
import ch.uzh.ifi.hase.soprafs24.entity.Guest;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GuestGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GuestPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GuestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Guest Controller
 * This class is responsible for handling all REST request that are related to
 * the guest.
 * The controller will receive the request and delegate the execution to the
 * GuestService and finally return the result.
 */
@RestController
public class GuestController {

    private final GuestService guestService;

    GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/guests")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GuestGetDTO> getAllGuests() {
        List<Guest> guests = guestService.getGuests();
        List<GuestGetDTO> guestGetDTOs = new ArrayList<>();
        for (Guest guest : guests) {
            guestGetDTOs.add(DTOMapper.INSTANCE.convertEntityToGuestGetDTO(guest));
        }
        return guestGetDTOs;
    }

    @GetMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GuestGetDTO getGuest(@PathVariable Long id) {
        Guest guest = guestService.getGuest(id);
        return DTOMapper.INSTANCE.convertEntityToGuestGetDTO(guest);
    }

    @PostMapping("/guests")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GuestGetDTO createGuest() {
        Guest guest = guestService.loginGuest();
        return DTOMapper.INSTANCE.convertEntityToGuestGetDTO(guest);
    }
    @DeleteMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GuestGetDTO logoutUser(@PathVariable Long id)  {
        Guest logged_in_guest = guestService.logoutUser(id);
        return DTOMapper.INSTANCE.convertEntityToGuestGetDTO(logged_in_guest);
    }
}
