package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.ReadyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReadyStatusServiceTest {

    @InjectMocks
    private ReadyStatusService readyStatusService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ReadyStatus readyStatus;

    @Mock
    private EntityTransaction transaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.find(eq(ReadyStatus.class), any())).thenReturn(readyStatus);
    }

    @Test
    public void testUpdateReadyStatus() {

        when(readyStatus.getReadyParticipants()).thenReturn(5);

        boolean result = readyStatusService.updateReadyStatus(123456, 5);

        assertEquals(5, readyStatus.getReadyParticipants());

        assertTrue(result);
    }

    @Test
    public void testGetReadyParticipants() {

        when(readyStatus.getReadyParticipants()).thenReturn(3);

        int result = readyStatusService.getReadyParticipants(123456);

        assertEquals(3, result);
    }

    @Test
    public void testResetReadyParticipants() {

        readyStatusService.resetReadyParticipants(123456);

        verify(readyStatus, times(1)).reset();
    }
}
