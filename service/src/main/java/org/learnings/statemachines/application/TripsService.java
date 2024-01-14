package org.learnings.statemachines.application;

import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.application.dto.BookingDTO;
import org.learnings.statemachines.application.dto.TripDTO;
import org.learnings.statemachines.application.error.InvalidStateTransition;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.learnings.statemachines.repositories.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.learnings.statemachines.domain.TripEvents.ARRIVES_AT_DESTINATION;
import static org.learnings.statemachines.domain.TripEvents.DRIVER_REQUESTS_TRIP;

@Component
@Slf4j
public class TripsService {

    private final TripCurrentStateRepository tripCurrentStateRepository;
    private final TripRepository tripRepository;
    private final StateMachineFactory<TripStates, TripEvents> stateMachineFactory;

    public TripsService(TripCurrentStateRepository tripCurrentStateRepository,
                        TripRepository tripRepository,
                        StateMachineFactory<TripStates, TripEvents> stateMachineFactory) {
        this.tripCurrentStateRepository = tripCurrentStateRepository;
        this.tripRepository = tripRepository;
        this.stateMachineFactory = stateMachineFactory;
    }

    public Optional<TripDTO> getTrip(String id) {
        Optional<TripEntity> tripEntity = tripRepository.findById(UUID.fromString(id));
        log.debug("retrieved trip with info [{}]", tripEntity);

        return Optional.ofNullable(TripDTO.fromEntity(tripEntity.orElse(null)));
    }

    public void createTrip(UUID id, String customerId, String location, String destination) {
        TripEntity trip = new TripEntity(id,
                customerId,
                location,
                destination,
                new BookingEntity());

        tripRepository.save(trip);

        TripCurrentStateEntity tripCurrentState = new TripCurrentStateEntity(id, TripStates.TRIP_CREATED);
        tripCurrentStateRepository.save(tripCurrentState);

        log.debug("created trip [{}]", trip);
    }

    public void updateTripState(UUID id, TripEvents event, BookingDTO bookingDTO) {
        TripCurrentStateEntity tripCurrentState = tripCurrentStateRepository.findById(id).orElseThrow();
        TripStates previousState = tripCurrentState.getState();

        StateMachine<TripStates, TripEvents> stateMachine = createTripMachineWithState(id, previousState);

        Message<TripEvents> eventMessage =
                event == DRIVER_REQUESTS_TRIP || event == ARRIVES_AT_DESTINATION ?
                        MessageBuilder.withPayload(event).setHeader("booking", bookingDTO).setHeader("trip-id", id).build() :
                        MessageBuilder.withPayload(event).build();
        boolean isNewEventAccepted = stateMachine.sendEvent(eventMessage);
        TripStates newState = stateMachine.getState().getId();
        if (!isNewEventAccepted)
            throw new InvalidStateTransition("Action [" + event + "] not allowed. Previous trip state was [" + previousState
                    + "] and current is [" + newState + "]");

        tripCurrentState = new TripCurrentStateEntity(id, newState);
        tripCurrentStateRepository.save(tripCurrentState);

        log.info("new state machine state: [{}]", newState);
    }

    private StateMachine<TripStates, TripEvents> createTripMachineWithState(UUID id, TripStates state) {
        log.debug("state machine with uuid: [{}]", id);
        log.debug("stored state: [{}]", state);

        StateMachine<TripStates, TripEvents> stateMachine = stateMachineFactory.getStateMachine(id);
        stateMachine.stop();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma ->
                        sma.resetStateMachine(
                                new DefaultStateMachineContext<>(state, null, null, null)));

        stateMachine.start();
        log.debug("state machine state: [{}]", stateMachine.getState().getId());

        return stateMachine;
    }
}
