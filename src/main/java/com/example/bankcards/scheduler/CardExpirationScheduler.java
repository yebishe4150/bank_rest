package com.example.bankcards.scheduler;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardExpirationScheduler {

    private final CardRepository cardRepository;

    /**
     * Каждый день в 03:00 проверяем истёкшие карты
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void markExpiredCards() {

        LocalDate today = LocalDate.now();

        List<Card> cards = cardRepository.findAll()
                .stream()
                .filter(card ->
                        card.getStatus() == CardStatus.ACTIVE &&
                                card.getExpirationDate().isBefore(today)
                )
                .toList();

        if (cards.isEmpty()) {
            log.info("Нет карт, срок действия которых истёк");
            return;
        }

        cards.forEach(card -> card.setStatus(CardStatus.EXPIRED));
        cardRepository.saveAll(cards);

        log.info("Обновлено карт со статусом EXPIRED: {}", cards.size());
    }
}
