package fi.veikkaus.demo.repository;

import fi.veikkaus.demo.model.Event;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WalletRepository extends R2dbcRepository<Event, UUID> {}
