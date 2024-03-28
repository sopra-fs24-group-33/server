package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class GameLobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Player> players;

    @OneToOne
    private Player admin;


    @OneToOne
    private Game gamestatus;

    @Column
    private String pin;
}
