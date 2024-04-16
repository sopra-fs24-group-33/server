package ch.uzh.ifi.hase.soprafs24.rest.dto;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import java.util.List;

public class GameGetDTO {
    private List<Integer> cardStack;
    private List<GamePlayer> players;
    private Integer currentCard;
    private Integer successfulMove;
    private Integer level;
    private Long id;

    public List<Integer> getCardStack() {
        return cardStack;
    }

    public void setCardStack(List<Integer> cardStack) {
        this.cardStack = cardStack;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<GamePlayer> players) {
        this.players = players;
    }

    public Integer getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Integer currentCard) {
        this.currentCard = currentCard;
    }

    public Integer getSuccessfulMove() {
        return successfulMove;
    }

    public void setSuccessfulMove(Integer successfulMove) {
        this.successfulMove = successfulMove;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
