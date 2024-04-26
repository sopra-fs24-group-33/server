package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
public class ReadyStatus {

	@Id
	private Integer lobbyPin;

	@Column(name = "ready_participants")
	private int readyParticipants;

	private int totalParticipants;

	// Constructors
	public ReadyStatus() {}

	public ReadyStatus(int lobbyPin) {
		this.lobbyPin = lobbyPin;
		this.readyParticipants = 0;
	}

	public int getLobbyPin() {
		return lobbyPin;
	}

	public void setLobbyPin(int lobbyPin) {
		this.lobbyPin = lobbyPin;
	}

	public int getReadyParticipants() {
		return readyParticipants;
	}

	public void setReadyParticipants(int readyParticipants) {
		this.readyParticipants = readyParticipants;
	}

	public int getTotalParticipants() {
		return totalParticipants;
	}

	public void setTotalParticipants(int totalParticipants) {
		this.totalParticipants = totalParticipants;
	}

	// Direct handling of updates
	public void incrementReadyParticipants() {
		this.readyParticipants++;
	}

	public void reset() {
		this.readyParticipants = 0;
	}
}


