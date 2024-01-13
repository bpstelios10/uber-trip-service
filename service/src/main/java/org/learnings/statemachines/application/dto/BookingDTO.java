package org.learnings.statemachines.application.dto;

import org.learnings.statemachines.repositories.BookingEntity;

public record BookingDTO(String driverId, float price) {
    public static BookingDTO fromEntity(BookingEntity entity) {
        if (entity == null) return null;
        return new BookingDTO(entity.getDriverId(), entity.getPrice());
    }
}
