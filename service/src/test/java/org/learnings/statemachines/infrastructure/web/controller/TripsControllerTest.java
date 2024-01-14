package org.learnings.statemachines.infrastructure.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.statemachines.application.TripsService;
import org.learnings.statemachines.application.dto.BookingDTO;
import org.learnings.statemachines.application.dto.TripDTO;
import org.learnings.statemachines.domain.TripEvents;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripsControllerTest {

    @Mock
    private TripsService service;

    @InjectMocks
    private TripsController controller;

    private final TripDTO expectedTrip = new TripDTO(
            UUID.fromString("f9734631-6833-4885-93c5-dd41679fc908"),
            "customer-id",
            "location",
            "destination",
            null);

    @Test
    void getTripById_succeeds_whenTripExists() {
        when(service.getTrip("test-id")).thenReturn(Optional.of(expectedTrip));

        ResponseEntity<TripDTO> response = controller.getTripById("test-id");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedTrip);
    }

    @Test
    void getTripById_succeeds_whenTripDoesNotExist() {
        when(service.getTrip("test-id")).thenReturn(Optional.empty());

        ResponseEntity<TripDTO> response = controller.getTripById("test-id");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getTripById_doesNotHandleExceptionsYet() {
        when(service.getTrip("test-id")).thenThrow(new RuntimeException("something went wrong"));

        assertThatThrownBy(() -> controller.getTripById("test-id"))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("something went wrong");
    }

    @Test
    void createTrip_succeeds() {
        TripsController.TripCreateRequestBody trip = new TripsController.TripCreateRequestBody(
                expectedTrip.id(),
                expectedTrip.customerId(),
                expectedTrip.location(),
                expectedTrip.destination());
        doNothing().when(service).createTrip(
                expectedTrip.id(),
                expectedTrip.customerId(),
                expectedTrip.location(),
                expectedTrip.destination());

        ResponseEntity<Void> response = controller.createTrip(trip);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createTrip_doesNotHandleExceptionsYet() {
        TripsController.TripCreateRequestBody trip = new TripsController.TripCreateRequestBody(
                expectedTrip.id(),
                expectedTrip.customerId(),
                expectedTrip.location(),
                expectedTrip.destination());
        doThrow(new RuntimeException("something went wrong")).when(service).createTrip(
                expectedTrip.id(),
                expectedTrip.customerId(),
                expectedTrip.location(),
                expectedTrip.destination());

        assertThatThrownBy(() -> controller.createTrip(trip))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("something went wrong");
    }

    @Test
    void sendEvent_succeeds_withRequestBody() {
        TripsController.SendEventRequestBody requestBody = new TripsController.SendEventRequestBody("driver-id", 0f);
        doNothing().when(service).updateTripState(expectedTrip.id(), TripEvents.DRIVER_REQUESTS_TRIP, new BookingDTO("driver-id", 0f));

        ResponseEntity<Void> response =
                controller.sendEvent("f9734631-6833-4885-93c5-dd41679fc908", "DRIVER_REQUESTS_TRIP", requestBody);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void sendEvent_succeeds_withoutRequestBody() {
        doNothing().when(service).updateTripState(expectedTrip.id(), TripEvents.DRIVER_REQUESTS_TRIP, null);

        ResponseEntity<Void> response =
                controller.sendEvent("f9734631-6833-4885-93c5-dd41679fc908", "DRIVER_REQUESTS_TRIP", null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void sendEvent_fails_whenStateTransitionNotAllowed() {
        doThrow(NoSuchElementException.class).when(service).updateTripState(expectedTrip.id(), TripEvents.DRIVER_REQUESTS_TRIP, null);

        ResponseEntity<Void> response =
                controller.sendEvent("f9734631-6833-4885-93c5-dd41679fc908", "DRIVER_REQUESTS_TRIP", null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void sendEvent_fails_whenEventDoesNotExist() {
        TripsController.SendEventRequestBody requestBody = new TripsController.SendEventRequestBody("driver-id", 0f);

        ResponseEntity<Void> response =
                controller.sendEvent("f9734631-6833-4885-93c5-dd41679fc908", "wrong-event-name", requestBody);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
