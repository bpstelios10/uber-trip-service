package org.learnings.statemachines.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.statemachines.domain.Booking;
import org.learnings.statemachines.domain.Trip;
import org.learnings.statemachines.domain.TripStates;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.StateMachine;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripsServiceTest {

    @Mock
    private StateMachine<TripStates, String> stateMachine;

    @InjectMocks
    private TripsService service;

    @Test
    void getTrip() {
        Trip expectedTrip = new Trip(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "customer-id",
                "location",
                "destination",
                new Booking("driver-id", 12.3f));

        Trip actualTrip = service.getTrip("whatever");

        assertThat(expectedTrip).isEqualTo(actualTrip);
    }

    @Test
    void createTrip() {
        doNothing().when(stateMachine).start();
        when(stateMachine.sendEvent("FOUND_DRIVER")).thenReturn(true);

        assertDoesNotThrow(() -> service.createTrip(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "customer-id",
                "location",
                "destination"));
    }
}
