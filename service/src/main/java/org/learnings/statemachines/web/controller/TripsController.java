package org.learnings.statemachines.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.domain.Booking;
import org.learnings.statemachines.domain.Trip;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/trips")
@Slf4j
public class TripsController {

    @GetMapping(path = "/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable String id) {
        log.debug("get trip request, with trip id [{}]", id);

        Booking dummyBooking = new Booking("driver-id", 12.3f);
        Trip dummyTrip = new Trip(UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "customer-id",
                "location",
                "destination",
                dummyBooking);
        log.debug("retrieved trip with info [{}]", dummyTrip);

        return ResponseEntity.ok(dummyTrip);
    }

    @PostMapping
    public ResponseEntity<Void> createTrip(@RequestBody TripCreateRequestBody requestBody) {
        log.debug("create trip request for trip [{}]", requestBody);

        Trip trip = new Trip(requestBody.id(),
                requestBody.customerId(),
                requestBody.location(),
                requestBody.destination(),
                new Booking("driver-id", 12.3f));
        log.debug("created trip [{}]", trip);

        return ResponseEntity.ok().build();
    }

    record TripCreateRequestBody(UUID id, String customerId, String location, String destination) {
    }
}
