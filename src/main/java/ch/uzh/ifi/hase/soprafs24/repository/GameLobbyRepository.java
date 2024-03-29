package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gamelobbyRepository")
public interface GameLobbyRepository extends JpaRepository<GameLobby, Long> {
}