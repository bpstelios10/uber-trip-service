package org.learnings.statemachines.application.error;

public class InvalidStateTransition extends RuntimeException {
    public InvalidStateTransition(String message) {
        super(message);
    }
}
