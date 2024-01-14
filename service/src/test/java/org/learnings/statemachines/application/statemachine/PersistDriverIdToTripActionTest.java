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
import org.springframework.statemachine.StateContext;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersistDriverIdToTripActionTest {

    @Mock
    private TripRepository repository;
    @InjectMocks
    private PersistDriverIdToTripAction action;
    @Mock
    StateContext<TripStates, TripEvents> context;

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

        assertThatNoException().isThrownBy(() -> action.execute(context));
    }

    @Test
    void execute_throwsException_whenTripIdHeaderMissing() {
        when(context.getMessageHeader("trip-id")).thenReturn(null);
        BookingDTO booking = new BookingDTO(" ", 0f);
        when(context.getMessageHeader("booking")).thenReturn(booking);

        assertThatThrownBy(() -> action.execute(context))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("failed to retrieve trip-id or booking");
    }

    @Test
    void execute_throwsException_whenBookingHeaderMissing() {
        UUID tripId = UUID.randomUUID();
        when(context.getMessageHeader("trip-id")).thenReturn(tripId);
        when(context.getMessageHeader("booking")).thenReturn(null);

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

        assertThatThrownBy(() -> action.execute(context))
                .isExactlyInstanceOf(NoSuchElementException.class)
                .hasMessage("No value present");
    }
}
