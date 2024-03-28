package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Game {
    @ElementCollection
    List<Integer> cards;
    @Id
    Long gamelobbyreference;
}


