package fi.veikkaus.demo.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletDto {
  private String name;
  private UUID playerId;
  private Double balance;
}

