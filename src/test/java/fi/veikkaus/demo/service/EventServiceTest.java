package fi.veikkaus.demo.service;

import fi.veikkaus.demo.exception.EventAlreadyExistsException;
import fi.veikkaus.demo.model.Event;
import fi.veikkaus.demo.repository.EventRepository;
import fi.veikkaus.demo.service.impl.EventServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
public class EventServiceTest {
  @InjectMocks
  private EventServiceImpl eventService;
  @Mock
  private EventRepository eventRepository;
  
  
  private final UUID eventId = UUID.randomUUID();
  private final UUID playerId = UUID.randomUUID();
  
  @BeforeEach
  void setUp() {
  }
  
  @Test
  void logEvent_ShouldSaveEvent_WhenEventDoesNotExist() {
    // Given:
    UUID playerId = UUID.randomUUID();
    UUID eventId = UUID.randomUUID();
    BigDecimal amount = BigDecimal.TEN;
    String eventType = "WIN";
    ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
    
    
    // When:
    Mockito
        .when(eventRepository.findById(eventId))
        .thenReturn(Mono.empty());
    Mockito
        .when(eventRepository.save(eventCaptor.capture()))
        .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
    
    StepVerifier
        .create(eventService.logEvent(playerId, eventId, eventType, amount))
        .expectNextMatches(savedEvent -> savedEvent
                                             .getPlayerId()
                                             .equals(playerId)
                                         && savedEvent
                                                .getAmount()
                                                .compareTo(amount) == 0
                                         && savedEvent
                                                .getEventType()
                                                .equals(eventType))
        .verifyComplete();
    
    // Then:
    Event capturedEvent = eventCaptor.getValue();
    Assertions.assertEquals(playerId, capturedEvent.getPlayerId());
    Assertions.assertEquals(amount, capturedEvent.getAmount());
    Assertions.assertEquals(eventType, capturedEvent.getEventType());
    Assertions.assertNotNull(capturedEvent.getTimestamp());
  }
  
  @Test
  void logEvent_ShouldThrowException_WhenEventAlreadyExists() {
    // Given:
    Event existingEvent = Event
                              .builder()
                              .id(eventId)
                              .playerId(playerId)
                              .timestamp(LocalDateTime.now())
                              .eventType("CHARGE")
                              .amount(BigDecimal.TEN)
                              .build();
    UUID playerId = UUID.randomUUID();
    UUID eventId = UUID.randomUUID();
    
    
    // Заглушка для существующего события
    Mockito
        .when(eventRepository.findById(eventId))
        .thenReturn(Mono.just(existingEvent));
    ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
    Mockito
        .when(eventRepository.save(eventCaptor.capture()))
        .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
    
    StepVerifier
        .create(eventService.logEvent(playerId, eventId, "WIN", BigDecimal.TEN))
        .expectError(EventAlreadyExistsException.class)
        .verify();
  }
}
