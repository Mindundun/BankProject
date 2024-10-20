package com.bankproject.bankproject.global.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse<T> {

    private Integer currentPage;

    private Long totalPage;

    private Long totalCount;

    private List<T> data;

    public static <T> PagingResponse<T> of(Integer currentPage, Integer sizeOfPage, Long totalCount, List<T> data) {
        Long totalPage = (totalCount - 1) / sizeOfPage + 1;
        return PagingResponse.<T>builder()
                .currentPage(currentPage)
                .totalPage(totalPage)
                .totalCount(totalCount)
                .data(data)
                .build();
    }

}
