package org.learnings.statemachines.domain;

import java.util.UUID;

public record Trip(UUID id, String customerId, String location, String destination, Booking booking) {
}
