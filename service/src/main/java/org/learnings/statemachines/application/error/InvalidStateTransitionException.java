package org.learnings.statemachines.application.error;

public class InvalidStateTransitionException extends StateMachineError {
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}
