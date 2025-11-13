package com.example.bankcards.mapper;

import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.entity.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    @Mapping(target = "fromCardId", source = "fromCard.id")
    @Mapping(target = "toCardId", source = "toCard.id")
    @Mapping(target = "currency", source = "fromCard.currency")
    TransferDto toDto(Transfer transfer);
}

