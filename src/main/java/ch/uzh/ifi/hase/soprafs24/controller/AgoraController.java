package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.service.AgoraService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AgoraController {

    private final AgoraService agoraService;
    
    public AgoraController(AgoraService agoraService)   {
        this.agoraService = agoraService;
    }
    @GetMapping("/agoratoken/{gamePin}/{playerId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String Token(@PathVariable int gamePin, @PathVariable Long playerId) {
        String token = agoraService.createToken(gamePin, playerId);
        return token;
    }
}
