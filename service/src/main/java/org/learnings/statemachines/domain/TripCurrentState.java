package org.learnings.statemachines.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class TripCurrentState {
    private final UUID tripId;
    private final TripStates state;

    public TripCurrentState(UUID tripId, TripStates state) {
        this.tripId = tripId;
        this.state = state;
    }
}
