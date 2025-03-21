package fi.veikkaus.demo.service;

import fi.veikkaus.demo.model.Event;
import java.math.BigDecimal;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface EventService {
  Mono<Event> logEvent(UUID playerId,
                       UUID eventId,
                       String eventType,
                       BigDecimal amount);
}
