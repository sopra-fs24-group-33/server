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

    public Game startGame(GameLobby lobby) {
        Set<GamePlayer> lobbyPlayers = lobby.getGamePlayers();
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

    public Set<Integer> getPlayingCards() {
        Set<Integer> playingCards = new HashSet<>();
        for (GamePlayer player : this.players) {
            playingCards.addAll(player.getCards());
        }
        return playingCards;
    }

    private void distributeShameToken() {
      for (GamePlayer player : this.players) {
        Set<Integer> myCards = player.getCards();
        for (Integer card : myCards) {
          if (card.equals(this.currentCard)) {
						Integer currentShameToken = player.getShame_tokens();
						player.setShame_tokens(currentShameToken + 1);
          }
        }
      }
    }

	private void deleteCard() {
		for (GamePlayer player : this.players) {
			if (player.getCards().contains(this.currentCard)) {
				player.getCards().remove(this.currentCard); // Directly remove from the set
				break; // Exit after the first match if only one card needs to be removed
			}
		}

		this.cardStack.remove(this.currentCard); // Directly remove from the set
	}


	private void doMove() {
        Set<Integer> playingCards = this.getPlayingCards();
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
		if (this.cardStack.size() >= this.level * this.players.size()) {
			Random rand = new Random();
			List<Integer> cardList = new ArrayList<>(this.getCards()); // Temporary list for random access

			for (GamePlayer player : this.players) {
				if (player.getCards() == null) {
					player.setCards(new HashSet<>());
				}
				for (int j = 0; j < this.level; j++) {
					if (cardList.size() > 0) {
						int randomIndex = rand.nextInt(cardList.size());
						Integer selectedCard = cardList.get(randomIndex);
						player.getCards().add(selectedCard); // Add to player's set of cards
						cardList.remove(selectedCard); // Remove from temporary list
					}
				}
			}
			this.setCards(new HashSet<>(cardList)); // Update the game's cards with remaining
		} else {
			this.setSuccessfulMove(3); // not enough cards -> end game
		}
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

    public Long getLobbyId() {
        return LobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        LobbyId = lobbyId;
    }

    public Set<Integer> getCardStack() {
        return cardStack;
    }

    public void setCardStack(Set<Integer> cardStack) {
        this.cardStack = cardStack;
    }
}



