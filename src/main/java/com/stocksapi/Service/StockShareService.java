package com.stocksapi.Service;

import com.stocksapi.DTO.StockShareDTO;
import com.stocksapi.Exception.APIException;
import com.stocksapi.Persistence.DAO.StockShareDAO;
import com.stocksapi.Persistence.DAO.UserDAO;
import com.stocksapi.Persistence.Entity.StockShareEntity;
import com.stocksapi.Persistence.Entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockShareService {

    private final StockShareDAO stockShareDAO;

    private final UserDAO userDAO;

    public StockShareDTO purchaseStock(StockShareDTO stockShareDTO) {
        StockShareEntity savedStockShare;
        UserEntity associatedUser = userDAO.getById(stockShareDTO.getUserId());
        if (stockShareDTO.getId() != null) {
            StockShareEntity stockShareEntity = stockShareDAO.getById(stockShareDTO.getId());
            Double newShareQuantity = stockShareDTO.getShareQuantity() + stockShareEntity.getShareQuantity();
            stockShareEntity.setShareQuantity(newShareQuantity);
            savedStockShare = stockShareDAO.save(stockShareEntity);
        } else {
            savedStockShare = stockShareDAO.save(
                StockShareEntity.builder()
                    .exchange(stockShareDTO.getExchange())
                    .name(stockShareDTO.getName())
                    .shareQuantity(stockShareDTO.getShareQuantity())
                    .symbol(stockShareDTO.getSymbol())
                    .user(associatedUser)
                .build()
            );
        }

        return StockShareDTO.builder()
                .id(savedStockShare.getId())
                .userId(associatedUser.getId())
                .exchange(savedStockShare.getExchange())
                .name(savedStockShare.getName())
                .shareQuantity(savedStockShare.getShareQuantity())
                .symbol(savedStockShare.getSymbol())
                .createdDate(savedStockShare.getCreatedDate())
                .lastModifiedDate(savedStockShare.getLastModifiedDate())
            .build();
    }

    public StockShareDTO sellStock(StockShareDTO stockShareDTO) {
        StockShareEntity existingStockShare = stockShareDAO.getById(stockShareDTO.getId());
        double subtractedShareQuantity = existingStockShare.getShareQuantity() - stockShareDTO.getShareQuantity();
        if (subtractedShareQuantity > 0) {
            existingStockShare.setShareQuantity(subtractedShareQuantity);
            StockShareEntity savedStockShare = stockShareDAO.save(existingStockShare);
            return StockShareDTO.builder()
                    .id(savedStockShare.getId())
                    .userId(stockShareDTO.getUserId())
                    .exchange(savedStockShare.getExchange())
                    .name(savedStockShare.getName())
                    .shareQuantity(savedStockShare.getShareQuantity())
                    .symbol(savedStockShare.getSymbol())
                    .createdDate(savedStockShare.getCreatedDate())
                    .lastModifiedDate(savedStockShare.getLastModifiedDate())
                .build();
        } else if (subtractedShareQuantity < 0) {
            throw new APIException("Stock sale quantity cannot exceed existing stock share quantity.", HttpStatus.BAD_REQUEST);
        } else {
            stockShareDAO.deleteById(stockShareDTO.getId());
            return null;
        }
    }
}
