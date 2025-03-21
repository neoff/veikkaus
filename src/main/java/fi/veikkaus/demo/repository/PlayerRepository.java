package fi.veikkaus.demo.repository;

import fi.veikkaus.demo.model.Player;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PlayerRepository extends ReactiveCrudRepository<Player, UUID> {
  Flux<Player> findAllByName(String name);
}
