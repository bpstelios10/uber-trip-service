package org.learnings.statemachines.infrastructure.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.domain.Trip;
import org.learnings.statemachines.application.TripsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/trips")
@Slf4j
public class TripsController {

    private final TripsService service;

    public TripsController(TripsService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable String id) {
        log.debug("get trip request, with trip id [{}]", id);

        Trip dummyTrip = service.getTrip(id);

        return ResponseEntity.ok(dummyTrip);
    }

    @PostMapping
    public ResponseEntity<Void> createTrip(@Valid @RequestBody TripCreateRequestBody requestBody) {
        log.debug("create trip request for trip [{}]", requestBody);

        service.createTrip(requestBody.id(),
                requestBody.customerId(),
                requestBody.location(),
                requestBody.destination());

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}/action/{event}")
    public ResponseEntity<Void> sendEvent(@PathVariable String id, @PathVariable String event, @RequestBody SendEventRequestBody requestBody) {
//        StateMachine<TripStates, String> stateMachine = orderStateMachineAdapter.restore(order);
//        if (stateMachine.sendEvent(event)) {
//            orderStateMachineAdapter.persist(stateMachine, order);
//            return ResponseEntity.accepted().build();
//        } else {
        return ResponseEntity.unprocessableEntity().build();
//        }
    }

    record TripCreateRequestBody(@NotNull UUID id, @NotBlank String customerId, @NotBlank String location, @NotBlank String destination) {
    }

    record SendEventRequestBody(String driverId) {
    }
}
