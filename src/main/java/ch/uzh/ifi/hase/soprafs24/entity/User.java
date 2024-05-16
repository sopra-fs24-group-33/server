package ch.uzh.ifi.hase.soprafs24.entity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import javax.persistence.*;

@Entity
public class User extends SuperUser {
    @Column(nullable = false, unique = true)
    String username;
    @Column(nullable = false)
    private UserStatus status;
    @Column(nullable = false)
    int gamesPlayed;
    @Column(nullable = false)
    int shame_tokens;
    @Column(nullable = false)
    int flawlessWins;
    @Column(nullable = false)
    int roundsWon;
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

    public int getFlawlessWins() {
        return flawlessWins;
    }

    public void setFlawlessWins(int flawless_wins) {
        this.flawlessWins = flawless_wins;
    }

    public int getRoundsWon() {
        return roundsWon;
    }

    public void setRoundsWon(int rounds_won) {
        this.roundsWon = rounds_won;
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
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public int getCurrent_shame_tokens() {
        return current_shame_tokens;
    }
    public void setCurrent_shame_tokens(int current_shame_tokens) {
        this.current_shame_tokens = current_shame_tokens;
    }

}
