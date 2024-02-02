package org.learnings.statemachines.application.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.application.dto.BookingDTO;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.learnings.statemachines.repositories.TripEntity;
import org.learnings.statemachines.repositories.TripRepository;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class PersistDriverIdToTripAction implements Action<TripStates, TripEvents> {

    private final TripRepository repository;

    public PersistDriverIdToTripAction(TripRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(StateContext<TripStates, TripEvents> stateContext) {
        log.debug("persist driver-id in trip?");

        try {
            UUID tripId = (UUID) stateContext.getMessageHeader("trip-id");
            BookingDTO bookingDTO = (BookingDTO) stateContext.getMessageHeader("booking");
            if (tripId == null || bookingDTO == null)
                throw new RuntimeException("failed to retrieve trip-id or booking");

            TripEntity tripEntity = repository.findById(tripId).orElseThrow();
            tripEntity.getBooking().setDriverId(bookingDTO.driverId());
            repository.save(tripEntity);
        } catch (Exception ex) {
            log.error("Exception caught when trying to update driver-id");
            stateContext.getStateMachine().setStateMachineError(ex);
            stateContext.getStateMachine().getExtendedState().getVariables().put("ERROR", ex);

            throw ex;
        }
    }
}
