package fi.veikkaus.demo.repository;

import fi.veikkaus.demo.model.Player;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PlayerRepository extends R2dbcRepository<Player, UUID> {}
