package fi.veikkaus.demo.service.impl;

import fi.veikkaus.demo.exception.EventAlreadyExistsException;
import fi.veikkaus.demo.model.Event;
import fi.veikkaus.demo.repository.EventRepository;
import fi.veikkaus.demo.service.EventService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
  private final EventRepository eventRepository;
  
  public Mono<Event> logEvent(UUID playerId,
                              UUID eventId,
                              String eventType,
                              BigDecimal amount) {
    Event event = Event
                      .builder()
                      .id(eventId)
                      .playerId(playerId)
                      .timestamp(LocalDateTime.now())
                      .eventType(eventType)
                      .amount(amount)
                      .build();
    if (eventId == null) {
      return eventRepository.save(event);
    }
    
    return eventRepository
               .findById(event.getId())
               .flatMap(existingEvent -> {
                 return Mono.error(new EventAlreadyExistsException("Event with ID " + event.getId() + " already exists"));
               })
               .switchIfEmpty(eventRepository.save(event))
               .cast(Event.class);
  }
}
