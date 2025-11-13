package com.example.bankcards.service;

import com.example.bankcards.entity.Transfer;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransferService {
    Transfer send(UUID userId, UUID fromCardId, UUID toCardId, BigDecimal amount);
}
