package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gamePlayerRepository")
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
}