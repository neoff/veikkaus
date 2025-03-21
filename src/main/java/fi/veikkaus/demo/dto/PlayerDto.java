package fi.veikkaus.demo.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerDto {
  private UUID eventId;
  private UUID playerId;
  private String name;
  private TypeEnum type;
  private BigDecimal amount;
  
  public enum TypeEnum {
    PURCHASE("purchase"),
    WIN("win");
    private String value;
    
    TypeEnum(String value) {
      this.value = value;
    }
    
    public String getValue() {
      return value;
    }
    
    public String toString() {
      return String.valueOf(value);
    }
    
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }
}

