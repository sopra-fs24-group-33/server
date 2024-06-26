package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import javax.persistence.*;

@Entity
public class Player extends SuperUser {
    public Player(User user) {
        this.setToken(user.getToken());
        this.setName(user.getUsername());
        this.setIsUser(user.getId());
    }
    public Player()  {
        super();
        this.setIsUser(null);
        this.setShame_tokens(0);
    }
    @Column
    String name;

    @Column
    int shame_tokens;

    @Column
    Long isUser;
    public String getName() {
        return name;
    }
    public void setName(String guestname) {
        this.name = guestname;
    }
    public int getShame_tokens() {
        return shame_tokens;
    }
    public void setShame_tokens(int shame_tokens) {
        this.shame_tokens = shame_tokens;
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

    public Long getIsUser() {
        return isUser;
    }

    public void setIsUser(Long user) {
        this.isUser = user;
    }
}
