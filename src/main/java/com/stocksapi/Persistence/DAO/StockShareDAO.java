package com.stocksapi.Persistence.DAO;

import com.stocksapi.Persistence.Entity.StockShareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface StockShareDAO extends JpaRepository<StockShareEntity, String> {
    Set<StockShareEntity> findAllByUserId(String userId);
    Optional<StockShareEntity> getBySymbolAndUserId(String symbol, String userId);
}
