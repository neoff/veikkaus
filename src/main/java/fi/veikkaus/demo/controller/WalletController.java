package fi.veikkaus.demo.controller;

import fi.veikkaus.demo.pojo.TransactionRequest;
import fi.veikkaus.demo.pojo.WalletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WalletController implements WalletApi {
  private final WalletResponse walletResponse = new WalletResponse();
  
  @Override
  public Mono<ResponseEntity<WalletResponse>> chargeGame(Mono<TransactionRequest> transactionRequest,
                                                         ServerWebExchange exchange) {
    return transactionRequest.flatMap(res -> {
      log.info("Charging game for player: {}", res.getPlayerId());
      return Mono.just(ResponseEntity.status(201).body(walletResponse
                                             .playerId(res.getPlayerId())
                                             .balance(0.0)));
    });
  }
  
  @Override
  public Mono<ResponseEntity<WalletResponse>> winGame(Mono<TransactionRequest> transactionRequest,
                                                      ServerWebExchange exchange) {
    return transactionRequest.flatMap(res -> {
      log.info("Player win: {}", res.getPlayerId());
      return Mono.just(ResponseEntity.status(201).body(walletResponse
                                             .playerId(res.getPlayerId())
                                             .balance(0.0)));
    });
  }
}
