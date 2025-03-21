package fi.veikkaus.demo.model;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("player")
@NoArgsConstructor
@AllArgsConstructor
public class Player {
  @Id
  private UUID id;
  private String name;
  private BigDecimal balance;
}


