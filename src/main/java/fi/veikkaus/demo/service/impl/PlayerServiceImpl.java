package fi.veikkaus.demo.service.impl;

import fi.veikkaus.demo.dto.PlayerDto;
import fi.veikkaus.demo.exception.InsufficientException;
import fi.veikkaus.demo.exception.NotFoundException;
import fi.veikkaus.demo.model.Player;
import fi.veikkaus.demo.repository.PlayerRepository;
import fi.veikkaus.demo.service.EventService;
import fi.veikkaus.demo.service.PlayerService;
import fi.veikkaus.demo.service.mapper.DtoModelMapper;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
  private final PlayerRepository playerRepository;
  private final EventService eventService;
  private final DtoModelMapper dtoModelMapper;
  
  public Mono<PlayerDto> createPlayer(String name,
                                      BigDecimal balance) {
    return playerRepository
               .save(Player
                         .builder()
                         .name(name)
                         .balance(balance)
                         .build())
               .flatMap(r -> Mono.just(dtoModelMapper.toDto(r)));
  }
  
  public Flux<PlayerDto> getPlayerByName(String name) {
    return playerRepository
               .findAllByName(name)
               .flatMap(r -> Mono.just(dtoModelMapper.toDto(r)));
  }
  
  private Mono<Player> getPlayer(UUID playerId) {
    
    return playerRepository
               .findById(playerId)
               .switchIfEmpty(Mono.error(new NotFoundException("Player not found: " + playerId)));
  }
  
  /**
   * Update player balance based on transaction type.
   *
   * @param dto player object.
   *
   * @return updated player.
   */
  public Mono<PlayerDto> updateBalance(PlayerDto dto) {
    String eventType = dto
                           .getType()
                           .getValue();
    return eventService
               .logEvent(dto.getPlayerId(), dto.getEventId(), eventType, dto.getAmount())
               .flatMap(p -> getPlayer(p.getPlayerId()).flatMap(player -> {
                 
                 
                 switch (dto.getType()) {
                   case WIN -> player.setBalance(player
                                                     .getBalance()
                                                     .add(dto.getAmount()));
                   case PURCHASE -> player.setBalance(player
                                                          .getBalance()
                                                          .subtract(dto.getAmount()));
                 }
                 
                 if (player
                         .getBalance()
                         .compareTo(BigDecimal.ZERO) < 0) {
                   return Mono.error(new InsufficientException("Insufficient balance"));
                 }
                 return playerRepository
                            .save(player)
                            .doOnNext(updatedPlayer -> log.info("💰 Player {} balance updated: {}",
                                                                player.getId(),
                                                                updatedPlayer.getBalance()));
               }))
               .flatMap(r -> Mono.just(dtoModelMapper.toDto(r)))
               .onErrorResume(e -> {
                 log.error("❌ Error update balance: {}", e.getMessage());
                 return Mono.error(e);
               });
  }
  
}
