package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReadyStatusTest {

    private ReadyStatus readyStatus;

    @BeforeEach
    public void setup() {
        readyStatus = new ReadyStatus(123456);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(123456, readyStatus.getLobbyPin());
        assertEquals(0, readyStatus.getReadyParticipants());
        assertEquals(0, readyStatus.getTotalParticipants());
    }

    @Test
    public void testSetters() {
        readyStatus.setLobbyPin(654321);
        readyStatus.setReadyParticipants(5);
        readyStatus.setTotalParticipants(10);

        assertEquals(654321, readyStatus.getLobbyPin());
        assertEquals(5, readyStatus.getReadyParticipants());
        assertEquals(10, readyStatus.getTotalParticipants());
    }

    @Test
    public void testIncrementReadyParticipants() {
        readyStatus.incrementReadyParticipants();
        assertEquals(1, readyStatus.getReadyParticipants());

        readyStatus.incrementReadyParticipants();
        assertEquals(2, readyStatus.getReadyParticipants());
    }

    @Test
    public void testReset() {
        readyStatus.incrementReadyParticipants();
        readyStatus.incrementReadyParticipants();
        assertEquals(2, readyStatus.getReadyParticipants());

        readyStatus.reset();
        assertEquals(0, readyStatus.getReadyParticipants());
    }
}