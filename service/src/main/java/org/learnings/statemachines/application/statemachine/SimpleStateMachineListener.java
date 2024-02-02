package org.learnings.statemachines.application.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleStateMachineListener extends StateMachineListenerAdapter<TripStates, TripEvents> {

    @Override
    public void stateMachineError(StateMachine<TripStates, TripEvents> stateMachine, Exception exception) {
        log.debug("State machine error was added: [{}]", exception.getMessage());
    }
}
