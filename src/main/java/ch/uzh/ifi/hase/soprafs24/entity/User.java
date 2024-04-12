package ch.uzh.ifi.hase.soprafs24.entity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import javax.persistence.*;

@Entity
public class User extends SuperUser {
    @Column(nullable = false, unique = true)
    String username;
    @Column(nullable = false)
    int gamesPlayed;
    @Column(nullable = false)
    int shame_tokens;
    @Column
    int current_shame_tokens;
    @Column(nullable = false)
    String password;
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
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public void setToken(String token)  {
        super.setToken(token);
    }

    public String getToken()    {
        return super.getToken();
    }

    public UserStatus getStatus() {
        return super.getStatus();
    }

    public void setStatus(UserStatus status) {
        super.setStatus(status);
    }
    public int getCurrent_shame_tokens() {
        return current_shame_tokens;
    }
    public void setCurrent_shame_tokens(int current_shame_tokens) {
        this.current_shame_tokens = current_shame_tokens;
    }

}
