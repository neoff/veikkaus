package fi.veikkaus.demo.controller;

import fi.veikkaus.demo.exception.UserExistException;
import fi.veikkaus.demo.pojo.CreatePlayerRequest;
import fi.veikkaus.demo.pojo.WalletResponse;
import fi.veikkaus.demo.service.PlayerService;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlayerController implements PlayerApi {
  private final PlayerService playerService;
  private final WalletResponse defaultWalletResponse = new WalletResponse();
  
  @Override
  public Mono<ResponseEntity<WalletResponse>> createPlayer(Mono<CreatePlayerRequest> createPlayerRequest,
                                                           ServerWebExchange exchange) {
    return createPlayerRequest.flatMap(request -> playerService
                                                      .getPlayerByName(request.getName())
                                                      .hasElements()
                                                      .flatMap(exists -> {
                                                        if (exists) {
                                                          log.warn("❌ Player already exists: {}", request.getName());
                                                          return Mono.error(new UserExistException("Player already exists"));
                                                        }
                                                        return playerService
                                                                   .createPlayer(request.getName(), BigDecimal.valueOf(50.0))
                                                                   .map(newPlayer -> {
                                                                     log.info("✅ Player created: {}", newPlayer.getName());
                                                                     return ResponseEntity
                                                                                .status(HttpStatus.CREATED)
                                                                                .body(new WalletResponse()
                                                                                          .playerId(newPlayer.getPlayerId())
                                                                                          .name(newPlayer.getName())
                                                                                          .balance(newPlayer
                                                                                                       .getAmount()
                                                                                                       .doubleValue()));
                                                                   });
                                                      }));
  }
  
  @Override
  public Mono<ResponseEntity<WalletResponse>> getPlayerBalance(UUID playerId,
                                                               ServerWebExchange exchange) {
    return Mono.just(ResponseEntity.ok(defaultWalletResponse
                                           .playerId(playerId)
                                           .balance(0.0)));
  }
}
