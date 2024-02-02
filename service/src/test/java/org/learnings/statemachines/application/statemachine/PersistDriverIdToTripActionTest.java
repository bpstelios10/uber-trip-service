package org.learnings.statemachines.application.statemachine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.statemachines.application.dto.BookingDTO;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.learnings.statemachines.repositories.BookingEntity;
import org.learnings.statemachines.repositories.TripEntity;
import org.learnings.statemachines.repositories.TripRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersistDriverIdToTripActionTest {

    @Mock
    private TripRepository repository;
    @InjectMocks
    private PersistDriverIdToTripAction action;
    @Mock
    StateContext<TripStates, TripEvents> context;
    @Mock
    private StateMachine<TripStates, TripEvents> stateMachine;
    @Mock
    private ExtendedState extendedState;

    @Test
    void execute() {
        UUID tripId = UUID.randomUUID();
        when(context.getMessageHeader("trip-id")).thenReturn(tripId);
        BookingDTO booking = new BookingDTO("test-driver-id", 0f);
        when(context.getMessageHeader("booking")).thenReturn(booking);
        BookingEntity bookingEntity = new BookingEntity();
        TripEntity tripEntity = new TripEntity(tripId, "customer-id", "location", "destination", bookingEntity);
        when(repository.findById(tripId)).thenReturn(Optional.of(tripEntity));
        tripEntity.getBooking().setDriverId("test-driver-id");
        when(repository.save(tripEntity)).thenReturn(tripEntity);

        assertDoesNotThrow(() -> action.execute(context));
    }

    @Test
    void execute_throwsException_whenTripIdHeaderMissing() {
        when(context.getMessageHeader("trip-id")).thenReturn(null);
        BookingDTO booking = new BookingDTO(" ", 0f);
        when(context.getMessageHeader("booking")).thenReturn(booking);

        when(context.getStateMachine()).thenReturn(stateMachine);
        doNothing().when(stateMachine).setStateMachineError(any());
        when(stateMachine.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(new HashMap<>());

        assertThatThrownBy(() -> action.execute(context))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("failed to retrieve trip-id or booking");
    }

    @Test
    void execute_throwsException_whenBookingHeaderMissing() {
        UUID tripId = UUID.randomUUID();
        when(context.getMessageHeader("trip-id")).thenReturn(tripId);
        when(context.getMessageHeader("booking")).thenReturn(null);

        when(context.getStateMachine()).thenReturn(stateMachine);
        doNothing().when(stateMachine).setStateMachineError(any());
        when(stateMachine.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(new HashMap<>());

        assertThatThrownBy(() -> action.execute(context))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("failed to retrieve trip-id or booking");
    }

    @Test
    void execute_throwsException_whenTripNotFound() {
        UUID tripId = UUID.randomUUID();
        when(context.getMessageHeader("trip-id")).thenReturn(tripId);
        BookingDTO booking = new BookingDTO("test-driver-id", 0f);
        when(context.getMessageHeader("booking")).thenReturn(booking);
        when(repository.findById(tripId)).thenReturn(Optional.empty());

        when(context.getStateMachine()).thenReturn(stateMachine);
        doNothing().when(stateMachine).setStateMachineError(any());
        when(stateMachine.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(new HashMap<>());

        assertThatThrownBy(() -> action.execute(context))
                .isExactlyInstanceOf(NoSuchElementException.class)
                .hasMessage("No value present");
    }

    @Test
    void execute_throwsException_whenRepositorySaveFailure() {
        UUID tripId = UUID.randomUUID();
        when(context.getMessageHeader("trip-id")).thenReturn(tripId);
        BookingDTO booking = new BookingDTO("test-driver-id", 0f);
        when(context.getMessageHeader("booking")).thenReturn(booking);
        BookingEntity bookingEntity = new BookingEntity();
        TripEntity tripEntity = new TripEntity(tripId, "customer-id", "location", "destination", bookingEntity);
        when(repository.findById(tripId)).thenReturn(Optional.of(tripEntity));
        tripEntity.getBooking().setDriverId("test-driver-id");
        when(repository.save(tripEntity)).thenThrow(new RuntimeException("db failure"));

        when(context.getStateMachine()).thenReturn(stateMachine);
        doNothing().when(stateMachine).setStateMachineError(any());
        when(stateMachine.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(new HashMap<>());

        assertThatThrownBy(() -> action.execute(context))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("db failure");
    }
}
