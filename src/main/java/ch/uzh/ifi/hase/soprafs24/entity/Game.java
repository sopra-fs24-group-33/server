package ch.uzh.ifi.hase.soprafs24.entity;
import ch.uzh.ifi.hase.soprafs24.service.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;
import java.util.Random;


@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int gamepin;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "array_element")
    private Set<Integer> cardStack = new HashSet<>();
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<GamePlayer> players = new HashSet<>();
    @Column
    private Integer currentCard;

    @Column
    private Integer successfulMove = 0;
    // 0 = neutral
    // 1 = correct move
    // 2 = wrong move
    // end game

    @Column
    private Integer level = 1;

    private Set<Integer> createStack() {
        Set<Integer> numbers = new HashSet<>();
        for (int i = 1; i < 100; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    public Set<Integer> getPlayingCards() {
        Set<Integer> playingCards = new HashSet<>();
        for (GamePlayer player : this.players) {
            playingCards.addAll(player.getCards());
        }
        return playingCards;
    }


	public Set<Integer> getCards() {
        return this.cardStack;
    }

    public void setCards(Set<Integer> cards) {
        this.cardStack = cards;
    }

    public Integer getCurrentCard() {
        return this.currentCard;
    }

    public void setCurrentCard(Integer card) {
        this.currentCard = card;
    }

    public Integer getSuccessfulMove() {
        return this.successfulMove;
    }

    public void setSuccessfulMove(Integer move) {
        this.successfulMove = move;
    }

    public Set<GamePlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(Set<GamePlayer> playerz) {
        this.players = playerz;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer lev) {
        this.level = lev;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGamepin() {
        return gamepin;
    }

    public void setGamepin(int gamepin) {
        this.gamepin = gamepin;
    }

    public Set<Integer> getCardStack() {
        return cardStack;
    }

    public void setCardStack(Set<Integer> cardStack) {
        this.cardStack = cardStack;
    }
}



