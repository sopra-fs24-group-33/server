package ch.uzh.ifi.hase.soprafs24.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private Long LobbyId;

    @ElementCollection
    @Column(name = "array_element")
    private List<Integer> cardStack;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GamePlayer> players = new ArrayList<>();
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

    private List<Integer> createStack() {
        List<Integer> numbers = new ArrayList<>();
        for (Integer i = 1; i < 100; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    public Game startGame(GameLobby lobby) {
        List<GamePlayer> lobbyPlayers = lobby.getGamePlayers();
        for (GamePlayer gamePlayer : lobbyPlayers) {
            this.players.add(gamePlayer);
            gamePlayer.setGame(this);
            gamePlayer.setShame_tokens(0);
        }
        this.setLevel(1);
        this.setSuccessfulMove(0);
        this.setCurrentCard(0);
        this.updateGamestatus(this.getCurrentCard());
        return this;
    }

    private void doRound() {
        if (this.currentCard == 0) {
            this.setCards(this.createStack());
            this.distributeCards();
        } else if (this.successfulMove == 2) {
            if (this.getPlayingCards().size() != 0) {
                List<Integer> toBeDeleted = new ArrayList<>(this.getPlayingCards());
                for (Integer i=0; i < toBeDeleted.size(); i++) {
                    this.setCurrentCard(toBeDeleted.get(i));
                    this.deleteCard();
                }
            }
            this.setSuccessfulMove(1);
            this.distributeCards();
        } else if (this.successfulMove == 3) {
            // end game
        } else {
            this.doMove();
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
        for (Integer i=0; i < this.players.size(); i++) {
            playingCards.addAll(this.players.get(i).getCards());
        }
        return playingCards;
    }

    private void distributeShameToken() {
      for (Integer i=0; i < this.players.size(); i++) {
        List<Integer> myCards = this.players.get(i).getCards();
        for (Integer j=0; j < myCards.size(); j++) {
          if (myCards.get(j) == this.currentCard) {
          Integer current = this.players.get(i).getShame_tokens();
          this.players.get(i).setShame_tokens(current +1);
          }
        }
      }
    }

    private void deleteCard() {
        for (Integer i=0; i < this.players.size(); i++) {
            for (Integer j=0; j < this.players.get(i).getCards().size(); j++) {
                if (this.players.get(i).getCards().get(j) == this.currentCard) {
                    List<Integer> myCards = this.players.get(i).getCards();
                    myCards.remove(Integer.valueOf(this.currentCard));
                    this.players.get(i).setCards(myCards);
                    break;
                }
            }
        }
        List<Integer> newStack = new ArrayList<>(this.getCards());
        for (Integer j=0; j < this.cardStack.size(); j++) {
            if (this.cardStack.get(j) == this.currentCard) {
                newStack.remove(Integer.valueOf(this.currentCard));
                this.setCards(newStack);
                break;
            }
        }
    }

    private void doMove() {
        List<Integer> playingCards = this.getPlayingCards();
        Integer minimum = Collections.min(playingCards);
        if (this.currentCard == minimum) {
            this.setSuccessfulMove(1);
            this.deleteCard();
            if (playingCards.size() == 1) {
                this.setLevel(this.getLevel()+1);
                this.distributeCards();
            }
        } else {
            this.distributeShameToken();
            this.setSuccessfulMove(2);
        }
    }

    private void distributeCards() {
        if (this.cardStack.size() >= this.level*this.players.size()) {
            for (Integer i=0; i<this.players.size(); i++){
                for (Integer j=0; j < this.level; j++) {
                    Random rand = new Random();
                    Integer randomIndex = rand.nextInt(this.getCards().size());
                    List<Integer> newStack = new ArrayList<>(this.getCards());
                    // this.cardStack.remove(Integer.valueOf(randomIndex));
                    if (this.players.get(i).getCards() == null || this.successfulMove == 2) {
                        List<Integer> myNewStack = new ArrayList<>();
                        myNewStack.add(this.getCards().get(randomIndex));
                        this.players.get(i).setCards(myNewStack);
                    } else {
                        List<Integer> myNewStack = new ArrayList<>(this.players.get(i).getCards());
                        myNewStack.add(this.getCards().get(randomIndex));
                        this.players.get(i).setCards(myNewStack);
                    }
                    newStack.remove(Integer.valueOf(this.getCards().get(randomIndex)));
                    this.setCards(newStack);
                }
            }
        } else {
            this.setSuccessfulMove(3);
            // not enough cards -> end game
        }
    }

    public List<Integer> getCards() {
        return this.cardStack;
    }

    public void setCards(List<Integer> cards) {
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

    public List<GamePlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(List<GamePlayer> playerz) {
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

    public Long getLobbyId() {
        return LobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        LobbyId = lobbyId;
    }

    public List<Integer> getCardStack() {
        return cardStack;
    }

    public void setCardStack(List<Integer> cardStack) {
        this.cardStack = cardStack;
    }
}



