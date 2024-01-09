package org.learnings.statemachines.infrastructure.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.statemachines.application.TripsService;
import org.learnings.statemachines.domain.Booking;
import org.learnings.statemachines.domain.Trip;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripsControllerTest {

    @Mock
    private TripsService service;

    @InjectMocks
    private TripsController controller;

    @Test
    void getTripById() {
        Trip expectedTrip = new Trip(
                UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
                "customer-id",
                "location",
                "destination",
                new Booking("driver-id", 12.3f));
        when(service.getTrip("test-id")).thenReturn(expectedTrip);

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
        doNothing().when(service).createTrip(
                trip.id(),
                trip.customerId(),
                trip.location(),
                trip.destination());

        ResponseEntity<Void> response = controller.createTrip(trip);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
