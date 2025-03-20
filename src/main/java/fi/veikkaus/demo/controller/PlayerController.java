package fi.veikkaus.demo.controller;

import fi.veikkaus.demo.pojo.CreatePlayerRequest;
import fi.veikkaus.demo.pojo.WalletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlayerController implements PlayerApi {
  private final WalletResponse walletResponse = new WalletResponse();
  
  @Override
  public Mono<ResponseEntity<WalletResponse>> createPlayer(Mono<CreatePlayerRequest> createPlayerRequest,
                                                           ServerWebExchange exchange) {
    return createPlayerRequest.map(player -> {
      log.info("Creating player: {}", player.getName());
      return ResponseEntity
                 .status(201)
                 .body(walletResponse
                                   .playerId(UUID.randomUUID())
                                   .balance(0.0));
    });
  }
  
  @Override
  public Mono<ResponseEntity<WalletResponse>> getPlayerBalance(UUID playerId,
                                                               ServerWebExchange exchange) {
    return Mono.just(ResponseEntity.ok(walletResponse
                                           .playerId(playerId)
                                           .balance(0.0)));
  }
}
