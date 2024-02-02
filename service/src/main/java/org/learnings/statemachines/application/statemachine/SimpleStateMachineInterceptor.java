package org.learnings.statemachines.application.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.learnings.statemachines.repositories.TripCurrentStateEntity;
import org.learnings.statemachines.repositories.TripCurrentStateRepository;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class SimpleStateMachineInterceptor extends StateMachineInterceptorAdapter<TripStates, TripEvents> {

    private final TripCurrentStateRepository tripCurrentStateRepository;

    public SimpleStateMachineInterceptor(TripCurrentStateRepository tripCurrentStateRepository) {
        this.tripCurrentStateRepository = tripCurrentStateRepository;
    }

    @Override
    public StateContext<TripStates, TripEvents> postTransition(StateContext<TripStates, TripEvents> stateContext) {
        log.debug("Post transition interceptor. Target state: [{}]", stateContext.getTarget().getId());

        try {
            TripCurrentStateEntity tripCurrentStateEntity =
                    new TripCurrentStateEntity((UUID) stateContext.getMessageHeader("trip-id"), stateContext.getTarget().getId());
            tripCurrentStateRepository.save(tripCurrentStateEntity);
        } catch (Exception ex) {
            log.error("failed to store new state with exception: [{}]", ex.getMessage(), ex);
            stateContext.getStateMachine().setStateMachineError(ex);
            stateContext.getStateMachine().getExtendedState().getVariables().put("ERROR", ex);
        }

        return stateContext;
    }
}
