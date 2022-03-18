package com.stocksapi.Service;

import com.stocksapi.DTO.StockShareDTO;
import com.stocksapi.Persistence.DAO.StockShareDAO;
import com.stocksapi.Persistence.DAO.UserDAO;
import com.stocksapi.Persistence.Entity.StockShareEntity;
import com.stocksapi.Persistence.Entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockShareServiceTest {

    private StockShareDTO stockShareDTO;
    private StockShareEntity savedStockShareEntity;
    private UserEntity userEntity;
    private final String stockShareID = UUID.randomUUID().toString();
    private final String userID = UUID.randomUUID().toString();

    @Mock
    private StockShareDAO stockShareDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private StockShareService stockShareService;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder().id(userID).build();

        stockShareDTO = StockShareDTO.builder()
                .exchange("NYSE")
                .userId(userID)
                .name("Microsoft")
                .symbol("MSFT")
                .shareQuantity(2.76)
            .build();

        savedStockShareEntity = StockShareEntity.builder()
                .id(stockShareID)
                .user(userEntity)
                .exchange("NYSE")
                .name("Microsoft")
                .symbol("MSFT")
                .shareQuantity(2.76)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
            .build();
    }

    @Test
    void purchaseStock_savesReceivedStockShareInDB_returnsStockSharePurchase() {
        given(stockShareDAO.save(any(StockShareEntity.class))).willReturn(savedStockShareEntity);
        given(userDAO.getById(userID)).willReturn(userEntity);

        StockShareDTO savedStockShareDTO = stockShareService.purchaseStock(stockShareDTO);

        assertThat(savedStockShareDTO.getId()).isEqualTo(stockShareID);
        assertThat(savedStockShareDTO.getUserId()).isEqualTo(userID);
        assertThat(savedStockShareDTO.getExchange()).isEqualTo(stockShareDTO.getExchange());
        assertThat(savedStockShareDTO.getName()).isEqualTo(stockShareDTO.getName());
        assertThat(savedStockShareDTO.getSymbol()).isEqualTo(stockShareDTO.getSymbol());
        assertThat(savedStockShareDTO.getShareQuantity()).isEqualTo(stockShareDTO.getShareQuantity());
        assertThat(savedStockShareDTO.getCreatedDate()).isBefore(LocalDateTime.now());
        assertThat(savedStockShareDTO.getLastModifiedDate()).isBefore(LocalDateTime.now());

        savedStockShareEntity.setId(null);
        savedStockShareEntity.setCreatedDate(null);
        savedStockShareEntity.setLastModifiedDate(null);
        then(stockShareDAO).should().save(savedStockShareEntity);
    }
}