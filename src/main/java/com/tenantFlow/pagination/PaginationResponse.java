package com.tenantFlow.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginationResponse<T> {

    private final List<T> content;

    private final int page;

    private final int size;

    private final long totalElements;

    private final int totalPages;

    private final boolean hasNext;

    private final boolean hasPrevious;
    public static <T> PaginationResponse<T> from(Page<T> page) {

        return new PaginationResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}