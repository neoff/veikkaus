package fi.veikkaus.demo.repository;

import fi.veikkaus.demo.model.Event;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EventRepository extends ReactiveCrudRepository<Event, UUID> {}
