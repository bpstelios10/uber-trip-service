package org.learnings.statemachines.repositories;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.learnings.statemachines.domain.TripStates;

import java.util.UUID;

@Entity
@Table(name = "tripCurrentState")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TripCurrentStateEntity {
    @Id
    private UUID tripId;
    private TripStates state;
}
