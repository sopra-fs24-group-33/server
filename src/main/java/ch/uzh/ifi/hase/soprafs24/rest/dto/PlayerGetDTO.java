package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

public class PlayerGetDTO {
    private Long id;
    private String name;
    private int shame_tokens;
    private String token;
    private Long isUser;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getShame_tokens() {
        return shame_tokens;
    }
    public void setShame_tokens(int shame_tokens) {
        this.shame_tokens = shame_tokens;
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

    public Long getIsUser() {
        return isUser;
    }

    public void setIsUser(Long user) {
        this.isUser = user;
    }
}
