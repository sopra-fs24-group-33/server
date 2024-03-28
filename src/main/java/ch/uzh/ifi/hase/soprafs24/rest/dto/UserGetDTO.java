package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import javax.persistence.Column;
import javax.persistence.Id;

public class UserGetDTO {

    private Long id;
    private String username;
    private int gamesPlayed;
    private int shame_tokens;
    private int current_shame_tokens;
    private String password;
    private String token;
    private UserStatus status;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    public int getShame_tokens() {
        return shame_tokens;
    }
    public void setShame_tokens(int shame_tokens) {
        this.shame_tokens = shame_tokens;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setToken(String token)  {
        this.token = token;
    }
    public String getToken()    {
        return token;
    }
    public UserStatus getStatus() {
        return status;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public int getCurrent_shame_tokens() {
        return this.current_shame_tokens;
    }

    public void setCurrent_shame_tokens(int current_shame_tokens) {
        this.current_shame_tokens = current_shame_tokens;
    }
}
