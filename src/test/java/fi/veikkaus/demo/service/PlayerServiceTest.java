package fi.veikkaus.demo.service;

import fi.veikkaus.demo.dto.PlayerDto;
import fi.veikkaus.demo.exception.InsufficientException;
import fi.veikkaus.demo.model.Event;
import fi.veikkaus.demo.model.Player;
import fi.veikkaus.demo.repository.PlayerRepository;
import fi.veikkaus.demo.service.impl.PlayerServiceImpl;
import fi.veikkaus.demo.service.mapper.DtoModelMapper;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({SpringExtension.class})
public class PlayerServiceTest {
  
  @Mock
  private PlayerRepository playerRepository;
  
  @Mock
  private EventService eventService;
  
  @Mock
  private DtoModelMapper dtoModelMapper;
  
  @InjectMocks
  private PlayerServiceImpl playerService;
  
  private final UUID playerId = UUID.randomUUID();
  private final UUID eventId = UUID.randomUUID();
  private Player testPlayer;
  private PlayerDto testPlayerDto;
  
  @BeforeEach
  void setUp() {
    testPlayer = Player
                     .builder()
                     .id(playerId)
                     .name("TestPlayer")
                     .balance(BigDecimal.valueOf(100))
                     .build();
    
    testPlayerDto = PlayerDto
                        .builder()
                        .playerId(playerId)
                        .eventId(eventId)
                        .type(PlayerDto.TypeEnum.WIN)
                        .amount(testPlayer.getBalance())
                        .build();
    
    // When:
    when(dtoModelMapper.toDto(any(Player.class))).thenAnswer(invocation -> {
      Player player = invocation.getArgument(0);
      return PlayerDto
                 .builder()
                 .playerId(player.getId())
                 .eventId(eventId)
                 .type(PlayerDto.TypeEnum.WIN)
                 .amount(player.getBalance())
                 .build();
    });
  }
  
  @Test
  void getPlayerByName_ShouldReturnPlayers_WhenPlayersExist() {
    // Given: Мок репозитория возвращает игрока
    when(playerRepository.findAllByName("TestPlayer")).thenReturn(Flux.just(testPlayer));
    
    // When & Then: Проверяем, что `getPlayerByName` вернет ожидаемый `PlayerDto`
    StepVerifier
        .create(playerService.getPlayerByName("TestPlayer"))
        .expectNext(testPlayerDto)
        .verifyComplete();
    
    // Проверяем, что `findAllByName` вызван ровно 1 раз
    verify(playerRepository, times(1)).findAllByName("TestPlayer");
  }
  
  @Test
  void getPlayerByName_ShouldReturnEmptyFlux_WhenNoPlayersExist() {
    // Given: Мок репозитория возвращает пустой Flux
    when(playerRepository.findAllByName("UnknownPlayer")).thenReturn(Flux.empty());
    
    // When & Then: Проверяем, что `getPlayerByName` возвращает пустой Flux
    StepVerifier
        .create(playerService.getPlayerByName("UnknownPlayer"))
        .expectNextCount(0)  // Ожидаем, что нет элементов
        .verifyComplete();
    
    // Проверяем, что `findAllByName` вызван ровно 1 раз
    verify(playerRepository, times(1)).findAllByName("UnknownPlayer");
  }
  
  @Test
  void createPlayer_ShouldSaveAndReturnPlayerDto() {
    // Given:
    
    Player player = Player
                        .builder()
                        .id(playerId)
                        .name("TestPlayer")
                        .balance(BigDecimal.valueOf(100))
                        .build();
    PlayerDto playerDto = PlayerDto
                              .builder()
                              .playerId(player.getId())
                              .eventId(eventId)
                              .type(PlayerDto.TypeEnum.WIN)
                              .amount(player.getBalance())
                              .build();
    when(playerRepository.findAllByName("TestPlayer")).thenReturn(Flux.just(player));
    when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(player));
    
    // When:
    StepVerifier
        .create(playerService.createPlayer("TestPlayer", BigDecimal.valueOf(100)))
        .expectNext(playerDto)
        .verifyComplete();
    
    // Then:
    verify(playerRepository, times(1)).save(any(Player.class));
  }
  
  @Test
  void getPlayerByName_ShouldReturnPlayers() {
    // Given:
    Player player = Player
                        .builder()
                        .id(playerId)
                        .name("TestPlayer")
                        .balance(BigDecimal.valueOf(100))
                        .build();
    when(playerRepository.findAllByName("TestPlayer")).thenReturn(Flux.just(player));
    when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(player));
    
    // When:
    StepVerifier
        .create(playerService.createPlayer("TestPlayer", BigDecimal.valueOf(100)))
        .expectNextMatches(dto -> dto
                                      .getAmount()
                                      .compareTo(BigDecimal.valueOf(100)) == 0)
        .verifyComplete();
    
    
    // Then:
    Mockito
        .verify(playerRepository, times(1))
        .save(any(Player.class));
  }
  
  @Test
  void updateBalance_ShouldIncreaseBalanceOnWin() {
    // Given:
    Player updatedPlayer = Player
                               .builder()
                               .id(playerId)
                               .name("TestPlayer")
                               .balance(testPlayer
                                            .getBalance()
                                            .add(BigDecimal.valueOf(50)))
                               .build();
    // When:
    when(eventService.logEvent(any(), any(), any(), any())).thenReturn(Mono.just(Event
                                                                                     .builder()
                                                                                     .playerId(testPlayer.getId())
                                                                                     .build()));
    when(playerRepository.findById(any(UUID.class))).thenReturn(Mono.just(testPlayer));
    when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(updatedPlayer));
    
    
    // Do:
    StepVerifier
        .create(playerService.updateBalance(testPlayerDto))
        .expectNextMatches(dto -> dto
                                      .getAmount()
                                      .compareTo(BigDecimal.valueOf(150)) == 0)
        .verifyComplete();
    
    
    // Then:
    verify(eventService, times(1)).logEvent(any(), any(), any(), any());
    verify(playerRepository, times(1)).findById(playerId);
    verify(playerRepository, times(1)).save(any(Player.class));
  }
  
  @Test
  void updateBalance_ShouldThrowExceptionOnInsufficientFunds() {
    // Given:
    PlayerDto insufficientFundsDto = PlayerDto
                                         .builder()
                                         .playerId(playerId)
                                         .eventId(eventId)
                                         .type(PlayerDto.TypeEnum.PURCHASE)
                                         .amount(BigDecimal.valueOf(20000))
                                         .build();
    
    when(eventService.logEvent(any(), any(), any(), any())).thenReturn(Mono.just(Event
                                                                                     .builder()
                                                                                     .playerId(testPlayer.getId())
                                                                                     .build()));
    when(playerRepository.findById(any(UUID.class))).thenReturn(Mono.just(testPlayer));
    when(playerRepository.save(any())).thenReturn(Mono.just(testPlayer));
    
    StepVerifier
        .create(playerService.updateBalance(insufficientFundsDto))
        .expectError(InsufficientException.class)
        .verify();
    
    verify(playerRepository, times(1)).findById(playerId);
    verify(playerRepository, never()).save(any());
  }
}
