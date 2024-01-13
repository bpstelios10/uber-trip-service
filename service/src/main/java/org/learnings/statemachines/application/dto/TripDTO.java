package org.learnings.statemachines.application.dto;

import org.learnings.statemachines.repositories.TripEntity;

import java.util.UUID;

public record TripDTO(UUID id, String customerId, String location, String destination, BookingDTO booking) {
    public static TripDTO fromEntity(TripEntity entity) {
        if (entity == null) return null;
        return new TripDTO(entity.getId(), entity.getCustomerId(), entity.getLocation(), entity.getDestination(), BookingDTO.fromEntity(entity.getBooking()));
    }
}
