package org.learnings.statemachines.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.learnings.statemachines.domain.TripStates.*;

class TripStatesTest {

    @Test
    void isStateTransitionValid_forExistingStates() {
        assertThat(isStateTransitionValid(TRIP_CREATED, DRIVER_ASSIGNED)).isTrue();
        assertThat(isStateTransitionValid(DRIVER_ASSIGNED, DRIVER_AT_PICKUP_POINT)).isTrue();
        assertThat(isStateTransitionValid(DRIVER_AT_PICKUP_POINT, TRIP_STARTED)).isTrue();
        assertThat(isStateTransitionValid(TRIP_STARTED, TRIP_ENDED)).isTrue();
        assertThat(isStateTransitionValid(TRIP_ENDED, TRIP_CREATED)).isFalse();
        assertThat(isStateTransitionValid(TRIP_CREATED, TRIP_ENDED)).isFalse();
    }
}
