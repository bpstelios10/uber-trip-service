package org.learnings.statemachines.application.config;

import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.learnings.statemachines.repositories.TripCurrentStateEntity;
import org.learnings.statemachines.repositories.TripCurrentStateRepository;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.UUID;

@Slf4j
public class SimpleStateMachineListener extends StateMachineListenerAdapter<TripStates, TripEvents> {

    private final TripCurrentStateRepository tripCurrentStateRepository;
    private final UUID id;

    public SimpleStateMachineListener(TripCurrentStateRepository tripCurrentStateRepository, UUID id) {
        this.tripCurrentStateRepository = tripCurrentStateRepository;
        this.id = id;
    }

    @Override
    public void stateChanged(State<TripStates, TripEvents> from, State<TripStates, TripEvents> to) {
        log.debug("Transitioned from [{}] to [{}]", from == null ? "none" : from.getId(), to.getId());

        TripCurrentStateEntity tripCurrentStateEntity = new TripCurrentStateEntity(id, to.getId());
        tripCurrentStateRepository.save(tripCurrentStateEntity);
    }
}
