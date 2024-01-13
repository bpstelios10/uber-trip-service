package org.learnings.statemachines.domain;

import java.util.UUID;

public record TripCurrentState(UUID tripId, TripStates state) {
}
