package fi.veikkaus.demo.service.mapper;

import fi.veikkaus.demo.dto.PlayerDto;
import fi.veikkaus.demo.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoModelMapper {
  
  @Mapping(source = "id",
           target = "playerId")
  @Mapping(source = "balance",
           target = "amount")
  PlayerDto toDto(Player player);
}
