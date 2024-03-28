package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Player {

    public Player() {
        throw new RuntimeException();
    }


    public Player(Guest guest)    {
        this.setId(guest.getId());
        this.setName(guest.getGuestname());

    }

    @Id
    private Long id;
    @ElementCollection
    private List<Integer> cards;
    @Column
    private String name;
    @Column
    private Integer shame_tokens;

    @Column
    Long gamelobbyreference;

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

    public Long getGamelobbyreference() {
        return gamelobbyreference;
    }

    public void setGamelobbyreference(Long gamelobbyreference) {
        this.gamelobbyreference = gamelobbyreference;
    }
}
