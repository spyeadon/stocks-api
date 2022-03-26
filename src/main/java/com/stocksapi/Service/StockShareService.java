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

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockShareService {

    private final StockShareDAO stockShareDAO;

    private final UserDAO userDAO;

    public StockShareDTO purchaseStock(StockShareDTO stockShareDTO) {
        StockShareEntity toSaveStockShare;
        StockShareEntity savedStockShareEntity = findExistingStockShare(stockShareDTO);
        UserEntity associatedUser = userDAO.getById(stockShareDTO.getUserId());
        if (savedStockShareEntity != null) {
            Double newShareQuantity = stockShareDTO.getShareQuantity() + savedStockShareEntity.getShareQuantity();
            savedStockShareEntity.setShareQuantity(newShareQuantity);
            toSaveStockShare = stockShareDAO.save(savedStockShareEntity);
        } else {
            toSaveStockShare = stockShareDAO.save(
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
                .id(toSaveStockShare.getId())
                .userId(associatedUser.getId())
                .exchange(toSaveStockShare.getExchange())
                .name(toSaveStockShare.getName())
                .shareQuantity(toSaveStockShare.getShareQuantity())
                .symbol(toSaveStockShare.getSymbol())
                .createdDate(toSaveStockShare.getCreatedDate())
                .lastModifiedDate(toSaveStockShare.getLastModifiedDate())
            .build();
    }

    protected StockShareEntity findExistingStockShare(StockShareDTO stockShareDTO) {
        if (stockShareDTO.getId() != null)
            return stockShareDAO.getById(stockShareDTO.getId());
        else
            return stockShareDAO.getBySymbolAndUserId(stockShareDTO.getSymbol(), stockShareDTO.getUserId())
                .orElse(null);
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

    public Set<StockShareDTO> getPortfolio(String userId) {
        Set<StockShareEntity> stockShareEntities = stockShareDAO.findAllByUserId(userId);
        if (stockShareEntities.size() > 0)
            return stockShareEntities.stream()
                    .map(share -> StockShareDTO.builder()
                            .id(share.getId())
                            .userId(share.getUser().getId())
                            .shareQuantity(share.getShareQuantity())
                            .exchange(share.getExchange())
                            .name(share.getName())
                            .createdDate(share.getCreatedDate())
                            .lastModifiedDate(share.getLastModifiedDate())
                            .symbol(share.getSymbol())
                        .build()
                    ).collect(Collectors.toSet());
        else return Set.of();
    }
}