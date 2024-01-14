package org.learnings.statemachines.application.statemachine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.statemachines.application.dto.BookingDTO;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.StateContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateDriverIdGuardTest {

    @Mock
    private StateContext<TripStates, TripEvents> context;

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

        assertThat(guard.evaluate(context)).isFalse();
    }

    @Test
    void evaluate_isFalse_whenBookingHasNullDriverId() {
        BookingDTO booking = new BookingDTO(null, 0f);
        when(context.getMessageHeader("booking")).thenReturn(booking);

        assertThat(guard.evaluate(context)).isFalse();
    }

    @Test
    void evaluate_isFalse_whenBookingHasEmptyDriverId() {
        BookingDTO booking = new BookingDTO(" ", 0f);
        when(context.getMessageHeader("booking")).thenReturn(booking);

        assertThat(guard.evaluate(context)).isFalse();
    }
}
