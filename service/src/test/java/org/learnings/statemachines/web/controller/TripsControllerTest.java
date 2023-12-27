package org.learnings.statemachines.web.controller;

import org.junit.jupiter.api.Test;
import org.learnings.statemachines.domain.Booking;
import org.learnings.statemachines.domain.Trip;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TripsControllerTest {

    private final TripsController controller = new TripsController();

    @Test
    void getTripById() {
        Trip expectedTrip = new Trip(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "customer-id",
                "location",
                "destination",
                new Booking("driver-id", 12.3f));

        ResponseEntity<Trip> response = controller.getTripById("test-id");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedTrip);
    }

    @Test
    void createTrip() {
        TripsController.TripCreateRequestBody trip = new TripsController.TripCreateRequestBody(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "customer-id",
                "location",
                "destination");

        ResponseEntity<Void> response = controller.createTrip(trip);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
