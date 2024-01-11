package org.learnings.statemachines.infrastructure.web.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

    @Test
    void handleExceptions() {
        Exception someException = new RuntimeException("test exception thrown");
        ResponseEntity<Void> expectedResponse = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        ResponseEntity<Void> actualResponse = controllerExceptionHandler.handleExceptions(someException);

        assertThat(expectedResponse).isEqualTo(actualResponse);
    }
}
