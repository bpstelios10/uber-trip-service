package org.learnings.statemachines.domain;

public enum TripStates {
    TRIP_CREATED(null),
    DRIVER_ASSIGNED(TRIP_CREATED),
    DRIVER_AT_PICKUP_POINT(DRIVER_ASSIGNED),
    TRIP_STARTED(DRIVER_AT_PICKUP_POINT),
    TRIP_ENDED(TRIP_STARTED);

    private final TripStates validPreviousState;

    TripStates(TripStates validPreviousState) {
        this.validPreviousState = validPreviousState;
    }

    public static boolean isStateTransitionValid(TripStates from, TripStates to) {
        return to.validPreviousState == from;
    }
}
