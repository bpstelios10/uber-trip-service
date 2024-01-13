package org.learnings.statemachines.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.learnings.statemachines.domain.TripEvents.*;

class TripEventsTest {

    @Test
    void get() {
        assertThat(TripEvents.get("DRIVER_REQUESTS_TRIP")).containsSame(DRIVER_REQUESTS_TRIP);
        assertThat(TripEvents.get("DRIVER_ARRIVES_AT_PICKUP")).containsSame(DRIVER_ARRIVES_AT_PICKUP);
        assertThat(TripEvents.get("TRIP_STARTS")).containsSame(TRIP_STARTS);
        assertThat(TripEvents.get("ARRIVES_AT_DESTINATION")).containsSame(ARRIVES_AT_DESTINATION);


        assertThat(TripEvents.get(" TRIP_STARTS ")).containsSame(TRIP_STARTS);
        assertThat(TripEvents.get(" trip_staRTs ")).containsSame(TRIP_STARTS);

        assertThat(TripEvents.get("TRIP_START")).isEmpty();
    }
}
