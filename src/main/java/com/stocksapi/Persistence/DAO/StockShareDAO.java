package com.stocksapi.Persistence.DAO;

import com.stocksapi.Persistence.Entity.StockShareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockShareDAO extends JpaRepository<StockShareEntity, String> {
}
