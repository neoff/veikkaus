package fi.veikkaus.demo.controller;

import fi.veikkaus.demo.controller.mapper.RequestDtoMapper;
import fi.veikkaus.demo.pojo.TransactionRequest;
import fi.veikkaus.demo.pojo.WalletResponse;
import fi.veikkaus.demo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameTransactionController implements WalletApi {
  private final PlayerService playerService;
  private final RequestDtoMapper requestDtoMapper;
  private final WalletResponse walletResponse = new WalletResponse();
  
  @Override
  public Mono<ResponseEntity<WalletResponse>> chargeGame(Mono<TransactionRequest> transactionRequest,
                                                         ServerWebExchange exchange) {
    return transactionRequest.flatMap(req -> {
      log.info("🎮 Charging game for player: {}, amount: {}", req.getPlayerId(), req.getAmount());
      req.setType(TransactionRequest.TypeEnum.PURCHASE);
      return playerService
                 .updateBalance(requestDtoMapper.toDto(req))
                 .map(player -> ResponseEntity
                                    .status(201)
                                    .body(walletResponse
                                              .playerId(player.getPlayerId())
                                              .balance(player
                                                           .getAmount()
                                                           .doubleValue())));
    });
  }
  
  @Override
  public Mono<ResponseEntity<WalletResponse>> winGame(Mono<TransactionRequest> transactionRequest,
                                                      ServerWebExchange exchange) {
    return transactionRequest.flatMap(req -> {
      log.info("🏆 Player won: {}, amount: {}", req.getPlayerId(), req.getAmount());
      req.setType(TransactionRequest.TypeEnum.WIN);
      return playerService
                 .updateBalance(requestDtoMapper.toDto(req))
                 .map(player -> ResponseEntity
                                    .status(201)
                                    .body(walletResponse
                                              .playerId(player.getPlayerId())
                                              .name(player.getName())
                                              .balance(player
                                                           .getAmount()
                                                           .doubleValue())));
    });
  }
}
