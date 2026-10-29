package com.architecture_solution.coffeeshop_order_management.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    @Schema(example = "0")
    @JsonProperty("page_num")
    private int pageNum;

    @Schema(example = "10")
    @JsonProperty("page_size")
    private int pageSize;

    @Schema(example = "5")
    @JsonProperty("total_page")
    private int totalPage;

    private T items;
}