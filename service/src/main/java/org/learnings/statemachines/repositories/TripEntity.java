package org.learnings.statemachines.repositories;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TripEntity {
    @Id
    private UUID id;
    private String customerId;
    private String location;
    private String destination;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;
}
