package com.stocksapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockShareDTO {
    private String id;
    private String userId;
    private String name;
    private String symbol;
    private Double shareQuantity;
    private String exchange;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
