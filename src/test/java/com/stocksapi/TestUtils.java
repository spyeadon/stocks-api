package com.stocksapi;

import com.stocksapi.DTO.StockShareDTO;
import com.stocksapi.Persistence.Entity.StockShareEntity;
import com.stocksapi.Persistence.Entity.UserEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TestUtils {

    private final String exchange = "NYSE";
    private final String symbol = "MSFT";
    private final String name = "Microsoft";
    private final Double shareQuantity = 2.76;

    public StockShareDTO stockShareDTOFactory(String stockShareID, String userID) {
        StockShareDTO stockShareDTO = StockShareDTO.builder()
                .exchange(exchange)
                .symbol(symbol)
                .name(name)
                .shareQuantity(shareQuantity)
                .build();
        if (stockShareID != null) stockShareDTO.setId(stockShareID);
        if (userID != null) stockShareDTO.setUserId(userID);
        return stockShareDTO;
    }

    public StockShareEntity stockShareEntityFactory(String stockShareID, String userID) {
        StockShareEntity stockShareEntity = StockShareEntity.builder()
                .exchange(exchange)
                .name(name)
                .symbol(symbol)
                .shareQuantity(shareQuantity)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
            .build();
        if (stockShareID != null) stockShareEntity.setId(stockShareID);
        if (userID != null) stockShareEntity.setUser(UserEntity.builder().id(userID).build());
        return stockShareEntity;
    }
}
