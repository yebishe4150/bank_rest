package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@Schema(description = "Постраничный ответ")
public class PageBaseResponse<T> {

    @Schema(description = "Список элементов страницы")
    private List<T> content;

    @Schema(description = "Номер страницы")
    private int page;

    @Schema(description = "Размер страницы")
    private int size;

    @Schema(description = "Всего элементов")
    private long totalElements;

    @Schema(description = "Всего страниц")
    private int totalPages;

    public static <T> PageBaseResponse<T> from(Page<T> page) {
        return PageBaseResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
