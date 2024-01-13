package org.learnings.statemachines.repositories;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@ConditionalOnProperty(value = "service.database.embedded", havingValue = "true")
public interface TripCurrentStateRepository extends JpaRepository<TripCurrentStateEntity, UUID> {
}
