package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;
import java.util.Random;


@Entity
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @Column(name = "array_element")
    private List<Integer> cardStack;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GamePlayer> gamePlayers = new ArrayList<>();
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

    public List<Integer> createStack() {
        List<Integer> numbers = new ArrayList<>();
        for (Integer i = 1; i < 100; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    public void doRound() {
        if (this.successfulMove == 0 ) { //|| this.successfulMove == 2) {
            this.setCards(this.createStack());
            distributeCards();
        } else {
            doMove();
            // continue round -> get new input card
        }
    }

    public Game updateGamestatus(Integer playedCard) {
        this.setCurrentCard(playedCard);
        this.doRound();
        return this;
    }

    public List<Integer> getPlayingCards() {
        List<Integer> playingCards = new ArrayList<>();
        for (Integer i = 0; i < this.gamePlayers.size(); i++) {
            playingCards.addAll(this.gamePlayers.get(i).getCards());
        }
        return playingCards;
    }

    public void deleteCardFromPlayer() {
        for (Integer i = 0; i < this.gamePlayers.size(); i++) {
            for (Integer j = 0; j < this.gamePlayers.get(i).getCards().size(); j++) {
                if (this.gamePlayers.get(i).getCards().get(j) == this.currentCard) {
                    List<Integer> myCards =this.gamePlayers.get(i).getCards();
                    myCards.remove(j);
                    this.gamePlayers.get(i).setCards(myCards);
                    break;
                }
            }
        }
    }

    public void doMove() {
        List<Integer> playingCards = getPlayingCards();
        if (Collections.min(playingCards) == this.currentCard) {
            this.setSuccessfulMove(1);
            this.deleteCardFromPlayer();
            if (playingCards.size() == 1) {
                this.setLevel(this.level+1);
            }
            this.doRound();
        } else {
            this.setSuccessfulMove(2);
            this.doRound();
        }
    }

    public void distributeCards() {
        if (this.cardStack.size() >= this.level*this.gamePlayers.size()) {
            List<Integer> indices = new ArrayList<>();
            List<Integer> randoms = new ArrayList<>();
            for (Integer i = 0; i<this.gamePlayers.size(); i++){
                Integer k = 0;
                while (indices.size() < this.level) {
                    Random rand = new Random();
                    Integer randomIndex = rand.nextInt(this.cardStack.size());
                    Integer randomElement = this.cardStack.get(randomIndex);
                    if (randoms.contains(randomElement)) {
                        randoms.set(k, randomElement);
                        k++;
                    }
                }
                this.gamePlayers.get(i).setCards(randoms);
                List<Integer> newStack = new ArrayList<>(this.cardStack);
                for (Integer x=0; x<this.level; x++) {
                    newStack.remove(indices.get(x));
                }
                this.setCards(newStack);
                indices = new ArrayList<>();
                randoms = new ArrayList<>();
            }
        } else {
            this.setSuccessfulMove(3);
            // not enough cards -> end game
        }
    }

    public List<Integer> getCards() {
        return cardStack;
    }

    public void setCards(List<Integer> cards) {
        this.cardStack = cards;
    }

    public Integer getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Integer card) {
        this.currentCard = card;
    }

    public Integer getSuccessfulMove() {
        return successfulMove;
    }

    public void setSuccessfulMove(Integer move) {
        this.successfulMove = move;
    }

    public List<GamePlayer> getPlayers() {
        return this.gamePlayers;
    }

    public void setPlayers(List<GamePlayer> playerz) {
        this.gamePlayers = playerz;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer lev) {
        this.level = lev;
    }

}



