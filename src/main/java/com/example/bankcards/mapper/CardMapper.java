package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.CardMaskUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        imports = {CardMaskUtil.class}
)
public abstract class CardMapper {

    @Autowired
    protected com.example.bankcards.util.CardEncryptor encryptor;

    @Mapping(
            target = "maskedNumber",
            expression = "java(CardMaskUtil.mask(encryptor.decrypt(card.getEncryptedNumber())))"
    )
    @Mapping(target = "status", expression = "java(card.getStatus().name())")
    @Mapping(target = "currency", source = "currency")
    public abstract CardDto toDto(Card card);

    @Mapping(
            target = "maskedNumber",
            expression = "java(CardMaskUtil.mask(encryptor.decrypt(card.getEncryptedNumber())))"
    )
    public abstract CreateCardResponse toCreateResponse(Card card);
}
