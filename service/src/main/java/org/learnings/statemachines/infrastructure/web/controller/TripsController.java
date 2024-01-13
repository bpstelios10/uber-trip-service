package org.learnings.statemachines.infrastructure.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.learnings.statemachines.application.TripsService;
import org.learnings.statemachines.application.dto.TripDTO;
import org.learnings.statemachines.domain.TripEvents;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
@Slf4j
public class TripsController { //TODO handle db exceptions

    private final TripsService service;

    public TripsController(TripsService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TripDTO> getTripById(@PathVariable String id) {
        log.debug("get trip request, with trip id [{}]", id);

        Optional<TripDTO> dummyTrip = service.getTrip(id);

        return ResponseEntity.ok(dummyTrip.orElse(null));
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
        Optional<TripEvents> newTripEvent = TripEvents.get(event);
        if (newTripEvent.isEmpty()) return ResponseEntity.badRequest().build();
        // TODO check id is uuid

        try {
            service.updateTripState(UUID.fromString(id), newTripEvent.get());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().build(); //TODO improve response
        }

        return ResponseEntity.ok().build();
    }

    record TripCreateRequestBody(@NotNull UUID id, @NotBlank String customerId, @NotBlank String location,
                                 @NotBlank String destination) {
    }

    record SendEventRequestBody(String driverId) {
    }
}
