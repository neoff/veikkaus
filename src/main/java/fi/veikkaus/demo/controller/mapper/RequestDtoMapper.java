package fi.veikkaus.demo.controller.mapper;

import fi.veikkaus.demo.dto.PlayerDto;
import fi.veikkaus.demo.pojo.TransactionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestDtoMapper {
  PlayerDto toDto(TransactionRequest request);
}
