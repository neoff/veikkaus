package fi.veikkaus.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("event")
@NoArgsConstructor
@AllArgsConstructor
public class Event {
  @Id
  private UUID id;
  private UUID playerId;
  private String eventType;
  private BigDecimal amount;
  private LocalDateTime timestamp;
}


