package org.learnings.statemachines.application.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.application.dto.BookingDTO;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidateDriverIdGuard implements Guard<TripStates, TripEvents> {
    @Override
    public boolean evaluate(StateContext<TripStates, TripEvents> context) {
        log.debug("checking if driver-id is valid?");
        BookingDTO booking = (BookingDTO) context.getMessageHeader("booking");

        if (booking == null || booking.driverId() == null || booking.driverId().isBlank()) {
            log.error("guard prevented transition");
            return false;
        }

        return true;
    }
}
