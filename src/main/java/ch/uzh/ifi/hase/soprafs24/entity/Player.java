package ch.uzh.ifi.hase.soprafs24.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    @ElementCollection
    private List<Integer> cards;
    @Column
    private String name;
    @Column
    private Integer shame_tokens;
    @ManyToOne
    @JsonIgnore
    private GameLobby gameLobby;

    @ManyToOne
    private Game game;

    public List<Integer> getCards() {
        return cards;
    }



    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getShame_tokens() {
        return shame_tokens;
    }

    public void setShame_tokens(Integer shame_tokens) {
        this.shame_tokens = shame_tokens;
    }

    public GameLobby getGameLobby() {
        return gameLobby;
    }

    public void setGameLobby(GameLobby gameLobby) {
        this.gameLobby = gameLobby;
    }
}
