package org.learnings.statemachines.application;

import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.domain.Booking;
import org.learnings.statemachines.domain.Trip;
import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class TripsService {

    private final StateMachine<TripStates, TripEvents> stateMachine;

    public TripsService(StateMachine<TripStates, TripEvents> stateMachine) {
        this.stateMachine = stateMachine;
    }

    public Trip getTrip(String id) {
        Booking dummyBooking = new Booking("driver-id", 12.3f);
        Trip dummyTrip = new Trip(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "customer-id",
                "location",
                "destination",
                dummyBooking);
        log.debug("retrieved trip with info [{}]", dummyTrip);

        return dummyTrip;
    }

    public void createTrip(UUID id, String customerId, String location, String destination) {
        Trip trip = new Trip(id,
                customerId,
                location,
                destination,
                new Booking());

        stateMachine.start();
        log.debug("created trip [{}]", trip);
    }
}
