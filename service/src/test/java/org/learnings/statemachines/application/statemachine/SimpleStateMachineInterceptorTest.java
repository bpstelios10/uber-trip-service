package org.learnings.statemachines.application.statemachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.learnings.statemachines.repositories.TripCurrentStateEntity;
import org.learnings.statemachines.repositories.TripCurrentStateRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleStateMachineInterceptorTest {

    @Mock
    private TripCurrentStateRepository tripCurrentStateRepository;
    private SimpleStateMachineInterceptor interceptor;
    @Mock
    private StateContext<TripStates, TripEvents> stateContext;
    @Mock
    private State<TripStates, TripEvents> state;
    private final UUID id = UUID.randomUUID();
    @Mock
    private StateMachine<TripStates, TripEvents> stateMachine;
    @Mock
    private ExtendedState extendedState;

    @BeforeEach
    void setup() {
        when(stateContext.getTarget()).thenReturn(state);
        when(state.getId()).thenReturn(TripStates.DRIVER_ASSIGNED);
        when(stateContext.getMessageHeader("trip-id")).thenReturn(id);

        interceptor = new SimpleStateMachineInterceptor(tripCurrentStateRepository);
    }

    @Test
    void stateChanged_succeeds() {
        TripCurrentStateEntity tripCurrentStateEntity = new TripCurrentStateEntity(id, TripStates.DRIVER_ASSIGNED);
        when(tripCurrentStateRepository.save(tripCurrentStateEntity)).thenReturn(tripCurrentStateEntity);

        StateContext<TripStates, TripEvents> contextReturned = interceptor.postTransition(stateContext);

        assertThat(stateContext).isEqualTo(contextReturned);
    }

    @Test
    void stateChanged_throwsException_whenRepositoryFails() {
        TripCurrentStateEntity tripCurrentStateEntity = new TripCurrentStateEntity(id, TripStates.DRIVER_ASSIGNED);
        RuntimeException dbFailed = new RuntimeException("db failed");
        when(tripCurrentStateRepository.save(tripCurrentStateEntity)).thenThrow(dbFailed);
        when(stateContext.getStateMachine()).thenReturn(stateMachine);
        doNothing().when(stateMachine).setStateMachineError(dbFailed);
        when(stateMachine.getExtendedState()).thenReturn(extendedState);
        when(extendedState.getVariables()).thenReturn(new HashMap<>());

        StateContext<TripStates, TripEvents> contextReturned = interceptor.postTransition(stateContext);

        assertThat(stateContext).isEqualTo(contextReturned);
    }
}
