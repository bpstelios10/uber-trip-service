package org.learnings.statemachines.application.statemachine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.statemachines.application.dto.BookingDTO;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateDriverIdGuardTest {

    @Mock
    private StateContext<TripStates, TripEvents> context;
    @Mock
    private StateMachine<TripStates, TripEvents> stateMachine;
    @Mock
    private ExtendedState extendedState;

    private final ValidateDriverIdGuard guard = new ValidateDriverIdGuard();

    @Test
    void evaluate_isTrue_whenBookingExists() {
        BookingDTO booking = new BookingDTO("test-driver-id", 10f);
        when(context.getMessageHeader("booking")).thenReturn(booking);

        assertThat(guard.evaluate(context)).isTrue();
    }

    @Test
    void evaluate_isFalse_whenBookingIsNull() {
        when(context.getMessageHeader("booking")).thenReturn(null);
        when(context.getStateMachine()).thenReturn(stateMachine);
        doNothing().when(stateMachine).setStateMachineError(any());
        when(stateMachine.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(new HashMap<>());

        assertThat(guard.evaluate(context)).isFalse();
    }

    @Test
    void evaluate_isFalse_whenBookingHasNullDriverId() {
        BookingDTO booking = new BookingDTO(null, 0f);
        when(context.getMessageHeader("booking")).thenReturn(booking);
        when(context.getStateMachine()).thenReturn(stateMachine);
        doNothing().when(stateMachine).setStateMachineError(any());
        when(stateMachine.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(new HashMap<>());

        assertThat(guard.evaluate(context)).isFalse();
    }

    @Test
    void evaluate_isFalse_whenBookingHasEmptyDriverId() {
        BookingDTO booking = new BookingDTO(" ", 0f);
        when(context.getMessageHeader("booking")).thenReturn(booking);
        when(context.getStateMachine()).thenReturn(stateMachine);
        doNothing().when(stateMachine).setStateMachineError(any());
        when(stateMachine.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(new HashMap<>());

        assertThat(guard.evaluate(context)).isFalse();
    }
}
