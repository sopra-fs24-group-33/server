package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;

import java.util.List;


@Entity
@Table(name = "USER")
public class Player implements Serializable {

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private List<Integer> cards;

  @Column(nullable = false)
  private int currentShameTokens;

  public List<Integer> getCards() {
    return cards;
  }

  public void setCards(List<Integer> cardz) {
    this.cards = cardz;
  }

  public void setUsername(String name) {
    this.username = name;
  }

  public int getCurrentShameTokens() {
    return currentShameTokens;
  }

  public void setCurrentShameTokens(int num) {
    this.currentShameTokens = num;
  }

}
