package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.ReadyStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class ReadyStatusService {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public boolean updateReadyStatus(Integer lobbyPin, int totalParticipants) {
		ReadyStatus readyStatus = entityManager.find(ReadyStatus.class, lobbyPin);
		if (readyStatus == null) {
			readyStatus = new ReadyStatus(lobbyPin);
			entityManager.persist(readyStatus);
		}
		readyStatus.setTotalParticipants(totalParticipants);
		readyStatus.incrementReadyParticipants();
		entityManager.merge(readyStatus);

		return readyStatus.getReadyParticipants() == totalParticipants;
	}

	@Transactional
	public int getReadyParticipants(Integer lobbyPin) {
		ReadyStatus readyStatus = entityManager.find(ReadyStatus.class, lobbyPin);
		return readyStatus != null ? readyStatus.getReadyParticipants() : 0;
	}

	@Transactional
	public void resetReadyParticipants(Integer lobbyPin) {
		ReadyStatus readyStatus = entityManager.find(ReadyStatus.class, lobbyPin);
		readyStatus.reset();
	}
}
