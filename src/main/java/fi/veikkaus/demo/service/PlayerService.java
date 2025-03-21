package fi.veikkaus.demo.service;

import fi.veikkaus.demo.dto.PlayerDto;
import java.math.BigDecimal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
  Mono<PlayerDto> createPlayer(String name,
                               BigDecimal balance);
  
  Flux<PlayerDto> getPlayerByName(String name);
  
  Mono<PlayerDto> updateBalance(PlayerDto dto);
  
}
