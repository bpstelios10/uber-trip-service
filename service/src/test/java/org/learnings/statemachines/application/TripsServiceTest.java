package org.learnings.statemachines.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.statemachines.application.dto.TripDTO;
import org.learnings.statemachines.application.statemachine.SimpleStateMachineInterceptor;
import org.learnings.statemachines.application.statemachine.SimpleStateMachineListener;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.learnings.statemachines.repositories.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripsServiceTest {

    @Mock
    private TripCurrentStateRepository tripCurrentStateRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private StateMachineFactory<TripStates, TripEvents> stateMachineFactory;
    @Mock
    private SimpleStateMachineInterceptor simpleStateMachineInterceptor;
    @Mock
    private SimpleStateMachineListener simpleStateMachineListener;
    @InjectMocks
    private TripsService service;

    private final TripEntity expectedTrip = new TripEntity(
            UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
            "customer-id",
            "location",
            "destination",
            new BookingEntity());

    @Test
    void getTrip_whenTripExists() {
        when(tripRepository.findById(expectedTrip.getId())).thenReturn(Optional.of(expectedTrip));

        Optional<TripDTO> actualTrip = service.getTrip("f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(actualTrip).isNotEmpty();
        assertThat(TripDTO.fromEntity(expectedTrip)).isEqualTo(actualTrip.get());
    }

    @Test
    void getTrip_whenTripDoesNotExist() {
        when(tripRepository.findById(expectedTrip.getId())).thenReturn(Optional.empty());

        Optional<TripDTO> actualTrip = service.getTrip("f9734631-6833-4885-93c5-dd41679fc908");

        assertThat(actualTrip).isEmpty();
    }

    @Test
    void createTrip() {
        when(tripRepository.save(expectedTrip)).thenReturn(expectedTrip);
        TripCurrentStateEntity tripCurrentState = new TripCurrentStateEntity(expectedTrip.getId(), TripStates.TRIP_CREATED);
        when(tripCurrentStateRepository.save(tripCurrentState)).thenReturn(tripCurrentState);

        assertDoesNotThrow(() -> service.createTrip(
                expectedTrip.getId(),
                expectedTrip.getCustomerId(),
                expectedTrip.getLocation(),
                expectedTrip.getDestination()));
    }

    @Test
    void createTrip_fails_whenTripSaveThrowsException() {
        when(tripRepository.save(expectedTrip)).thenThrow(new RuntimeException("something went wrong"));

        assertThatThrownBy(() -> service.createTrip(
                expectedTrip.getId(),
                expectedTrip.getCustomerId(),
                expectedTrip.getLocation(),
                expectedTrip.getDestination()))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("something went wrong");

        verifyNoInteractions(tripCurrentStateRepository);
    }

    @Test
    void createTrip_fails_whenTripStateSaveThrowsException() {
        when(tripRepository.save(expectedTrip)).thenReturn(expectedTrip);
        TripCurrentStateEntity tripCurrentState = new TripCurrentStateEntity(expectedTrip.getId(), TripStates.TRIP_CREATED);
        when(tripCurrentStateRepository.save(tripCurrentState)).thenThrow(new RuntimeException("something more went wrong"));

        assertThatThrownBy(() -> service.createTrip(
                expectedTrip.getId(),
                expectedTrip.getCustomerId(),
                expectedTrip.getLocation(),
                expectedTrip.getDestination()))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("something more went wrong");
    }

//    TODO need to write component tests for this one
//    @Test
//    void updateState() {}
}
