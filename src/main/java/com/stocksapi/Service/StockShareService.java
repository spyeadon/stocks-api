package com.stocksapi.Service;

import com.stocksapi.DTO.StockShareDTO;
import com.stocksapi.Persistence.DAO.StockShareDAO;
import com.stocksapi.Persistence.Entity.StockShareEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockShareService {

    private final StockShareDAO stockShareDAO;

    public StockShareDTO purchaseStock(StockShareDTO stockShareDTO) {
        StockShareEntity savedStockShare = stockShareDAO.save(
            StockShareEntity.builder()
                .exchange(stockShareDTO.getExchange())
                .name(stockShareDTO.getName())
                .shareQuantity(stockShareDTO.getShareQuantity())
                .symbol(stockShareDTO.getSymbol())
            .build()
        );

        return StockShareDTO.builder()
                .id(savedStockShare.getId())
                .exchange(savedStockShare.getExchange())
                .name(savedStockShare.getName())
                .shareQuantity(savedStockShare.getShareQuantity())
                .symbol(savedStockShare.getSymbol())
                .createdDate(savedStockShare.getCreatedDate())
                .lastModifiedDate(savedStockShare.getLastModifiedDate())
            .build();
    }

    public StockShareDTO sellStock(StockShareDTO stockShareDTO) {
        return null;
    }
}
