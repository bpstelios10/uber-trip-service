package org.learnings.statemachines.infrastructure.web.error;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learnings.statemachines.application.error.InvalidStateTransitionException;
import org.learnings.statemachines.application.error.StateMachineError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.learnings.statemachines.utils.AssertionUtils.*;

class ControllerExceptionHandlerTest {

    private ListAppender<ILoggingEvent> logWatcher;
    private final ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

    @BeforeEach
    void setUp() {
        logWatcher = getLoggingEventsListAppender(ControllerExceptionHandler.class);
    }

    @AfterEach
    void teardown() {
        detachAllLoggingAppenders(ControllerExceptionHandler.class);
    }

    @Test
    void handleExceptions() {
        Exception someException = new RuntimeException("test exception thrown");
        ResponseEntity<Void> expectedResponse = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        ResponseEntity<Void> actualResponse = controllerExceptionHandler.handleExceptions(someException);

        assertThat(expectedResponse).isEqualTo(actualResponse);
        assertMessageWasInLogs(logWatcher, "Unexpected Exception: test exception thrown", Level.ERROR);
    }

    @Test
    void handleInvalidStateTransitionException() {
        InvalidStateTransitionException someException = new InvalidStateTransitionException("test InvalidStateTransitionException thrown");
        ResponseEntity<Void> expectedResponse = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();

        ResponseEntity<Void> actualResponse = controllerExceptionHandler.handleInvalidStateTransitionException(someException);

        assertThat(expectedResponse).isEqualTo(actualResponse);
        assertOnlyMessageInLogs(logWatcher, "Exception thrown: test InvalidStateTransitionException thrown", Level.ERROR);
        assertExceptionStacktraceInLogs(logWatcher, Level.ERROR);
    }

    @Test
    void handleStateMachineError() {
        StateMachineError someException = new StateMachineError("test StateMachineError thrown");
        ResponseEntity<Void> expectedResponse = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();

        ResponseEntity<Void> actualResponse = controllerExceptionHandler.handleStateMachineError(someException);

        assertThat(expectedResponse).isEqualTo(actualResponse);
        assertOnlyMessageInLogs(logWatcher, "Exception thrown: test StateMachineError thrown", Level.ERROR);
        assertExceptionStacktraceInLogs(logWatcher, Level.ERROR);
    }
}
