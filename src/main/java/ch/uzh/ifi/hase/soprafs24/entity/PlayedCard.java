package ch.uzh.ifi.hase.soprafs24.entity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.*;

@Entity
public class PlayedCard implements Serializable {
    @Id
    private Integer card;

    public Integer getCard() {
        return card;
    }

    public void setCard(Integer card) {
        this.card = card;
    }
}
