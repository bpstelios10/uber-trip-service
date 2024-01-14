package org.learnings.statemachines.application.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.learnings.statemachines.repositories.TripCurrentStateEntity;
import org.learnings.statemachines.repositories.TripCurrentStateRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.state.State;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleStateMachineListenerTest {

    @Mock
    private TripCurrentStateRepository tripCurrentStateRepository;
    private final UUID id = UUID.randomUUID();
    private SimpleStateMachineListener listener;
    @Mock
    private State<TripStates, TripEvents> from;
    @Mock
    private State<TripStates, TripEvents> to;

    @BeforeEach
    void setup() {
        listener = new SimpleStateMachineListener(tripCurrentStateRepository, id);
    }

    @Test
    void stateChanged() {
        when(to.getId()).thenReturn(TripStates.DRIVER_ASSIGNED);
        TripCurrentStateEntity tripCurrentStateEntity = new TripCurrentStateEntity(id, TripStates.DRIVER_ASSIGNED);
        when(tripCurrentStateRepository.save(tripCurrentStateEntity)).thenReturn(tripCurrentStateEntity);

        assertDoesNotThrow(() -> listener.stateChanged(from, to));
    }
}
