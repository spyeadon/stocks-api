package com.stocksapi.Service;

import com.stocksapi.DTO.StockShareDTO;
import com.stocksapi.Exception.APIException;
import com.stocksapi.Persistence.DAO.StockShareDAO;
import com.stocksapi.Persistence.DAO.UserDAO;
import com.stocksapi.Persistence.Entity.StockShareEntity;
import com.stocksapi.Persistence.Entity.UserEntity;
import com.stocksapi.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class StockShareServiceTest {

    private StockShareDTO stockShareDTO;
    private StockShareEntity savedStockShareEntity;
    private UserEntity userEntity;
    private final String stockShareID = UUID.randomUUID().toString();
    private final String userID = UUID.randomUUID().toString();
    private final Double shareQuantity = 2.76;

    private final TestUtils testUtils = new TestUtils();

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
                .shareQuantity(shareQuantity)
            .build();

        savedStockShareEntity = StockShareEntity.builder()
                .id(stockShareID)
                .user(userEntity)
                .exchange("NYSE")
                .name("Microsoft")
                .symbol("MSFT")
                .shareQuantity(shareQuantity)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
            .build();
    }

    @Test
    void purchaseStock_savesInitialStockShareInDB_returnsStockSharePurchase() {
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

        then(stockShareDAO).should(never()).getById(anyString());
        then(stockShareDAO).should().save(any(StockShareEntity.class));
    }

    @Test
    void purchaseStock_whenAlreadyPurchasedStockRequestWithNoID_addsSharesToExistingStock() {
        given(userDAO.getById(userID)).willReturn(userEntity);
        given(stockShareDAO.getBySymbolAndUserId(stockShareDTO.getSymbol(), userID)).willReturn(java.util.Optional.ofNullable(savedStockShareEntity));
        given(stockShareDAO.save(any(StockShareEntity.class))).willReturn(savedStockShareEntity);

        StockShareDTO savedStockShareDTO = stockShareService.purchaseStock(stockShareDTO);

        assertThat(savedStockShareDTO.getId()).isEqualTo(stockShareID);
        assertThat(savedStockShareDTO.getUserId()).isEqualTo(userID);
        assertThat(savedStockShareDTO.getExchange()).isEqualTo(stockShareDTO.getExchange());
        assertThat(savedStockShareDTO.getName()).isEqualTo(stockShareDTO.getName());
        assertThat(savedStockShareDTO.getSymbol()).isEqualTo(stockShareDTO.getSymbol());
        assertThat(savedStockShareDTO.getShareQuantity()).isEqualTo(shareQuantity * 2);
        assertThat(savedStockShareDTO.getCreatedDate()).isBefore(LocalDateTime.now());
        assertThat(savedStockShareDTO.getLastModifiedDate()).isBefore(LocalDateTime.now());

        then(stockShareDAO).should(never()).getById(anyString());
        then(stockShareDAO).should().getBySymbolAndUserId(stockShareDTO.getSymbol(), stockShareDTO.getUserId());
        then(stockShareDAO).should().save(any(StockShareEntity.class));
    }

    @Test
    void purchaseStock_savesIncreasedStockShareInDB_returnsStockSharePurchaseWithMoreShares() {
        given(userDAO.getById(userID)).willReturn(userEntity);

        stockShareDTO.setId(stockShareID);
        given(stockShareDAO.getById(stockShareID)).willReturn(savedStockShareEntity);

        StockShareEntity updatedStockShareEntity = StockShareEntity.builder()
                .id(stockShareID)
                .user(userEntity)
                .exchange("NYSE")
                .name("Microsoft")
                .symbol("MSFT")
                .shareQuantity(shareQuantity + shareQuantity)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        given(stockShareDAO.save(any(StockShareEntity.class))).willReturn(updatedStockShareEntity);

        //when
        StockShareDTO savedStockShareDTO = stockShareService.purchaseStock(stockShareDTO);

        then(stockShareDAO).should().getById(stockShareID);
        then(stockShareDAO).should(never()).getBySymbolAndUserId(anyString(), anyString());
        then(stockShareDAO).should().save(any(StockShareEntity.class));

        assertThat(savedStockShareDTO.getId()).isEqualTo(stockShareID);
        assertThat(savedStockShareDTO.getUserId()).isEqualTo(userID);
        assertThat(savedStockShareDTO.getExchange()).isEqualTo(stockShareDTO.getExchange());
        assertThat(savedStockShareDTO.getName()).isEqualTo(stockShareDTO.getName());
        assertThat(savedStockShareDTO.getSymbol()).isEqualTo(stockShareDTO.getSymbol());
        assertThat(savedStockShareDTO.getShareQuantity()).isGreaterThan(stockShareDTO.getShareQuantity());
        assertThat(savedStockShareDTO.getShareQuantity()).isEqualTo(updatedStockShareEntity.getShareQuantity());
        assertThat(savedStockShareDTO.getCreatedDate()).isBefore(LocalDateTime.now());
        assertThat(savedStockShareDTO.getLastModifiedDate()).isBefore(LocalDateTime.now());
    }

    @Test
    void sellStock_whenPartialStockSale_savesUpdateAndRespondsWithResource() {
        Double previousShareQuantity = 6.8;
        StockShareEntity previouslySavedStockShare = StockShareEntity.builder()
                .id(stockShareID)
                .user(userEntity)
                .exchange("NYSE")
                .name("Microsoft")
                .symbol("MSFT")
                .shareQuantity(previousShareQuantity)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
            .build();
        given(stockShareDAO.getById(stockShareID)).willReturn(previouslySavedStockShare);

        savedStockShareEntity.setShareQuantity(previousShareQuantity - shareQuantity);
        given(stockShareDAO.save(any(StockShareEntity.class))).willReturn(savedStockShareEntity);

        stockShareDTO.setId(stockShareID);
        stockShareDTO.setLastModifiedDate(LocalDateTime.now());
        stockShareDTO.setCreatedDate(LocalDateTime.now());

        //when
        StockShareDTO savedStockShareDTO = stockShareService.sellStock(stockShareDTO);

        then(stockShareDAO).should().getById(stockShareID);
        then(stockShareDAO).should().save(any(StockShareEntity.class));

        assertThat(savedStockShareDTO.getShareQuantity()).isLessThan(previousShareQuantity);
        assertThat(savedStockShareDTO.getShareQuantity()).isEqualTo(previousShareQuantity - shareQuantity);
        assertThat(savedStockShareDTO.getId()).isEqualTo(stockShareDTO.getId());
        assertThat(savedStockShareDTO.getUserId()).isEqualTo(stockShareDTO.getUserId());
        assertThat(savedStockShareDTO.getName()).isEqualTo(stockShareDTO.getName());
        assertThat(savedStockShareDTO.getSymbol()).isEqualTo(stockShareDTO.getSymbol());
        assertThat(savedStockShareDTO.getExchange()).isEqualTo(stockShareDTO.getExchange());
        assertThat(savedStockShareDTO.getCreatedDate()).isBefore(stockShareDTO.getCreatedDate());
        assertThat(savedStockShareDTO.getLastModifiedDate()).isBefore(stockShareDTO.getLastModifiedDate());
    }

    @Test
    void sellStock_whenFullStockSale_savesUpdateAndRespondsWithResource() {
        given(stockShareDAO.getById(stockShareID)).willReturn(savedStockShareEntity);

        stockShareDTO.setId(stockShareID);
        stockShareDTO.setLastModifiedDate(LocalDateTime.now());
        stockShareDTO.setCreatedDate(LocalDateTime.now());

        //when
        StockShareDTO savedStockShareDTO = stockShareService.sellStock(stockShareDTO);

        then(stockShareDAO).should(never()).save(any(StockShareEntity.class));
        then(stockShareDAO).should().deleteById(stockShareID);

        assertThat(savedStockShareDTO).isNull();
    }

    @Test
    void sellStock_whenRequestIsGTExistingShare_throwsApiException() {
        stockShareDTO.setId(stockShareID);
        savedStockShareEntity.setShareQuantity(shareQuantity - 1);
        given(stockShareDAO.getById(stockShareID)).willReturn(savedStockShareEntity);

        String exceptionMessage = "Stock sale quantity cannot exceed existing stock share quantity.";
        HttpStatus exceptionStatus = HttpStatus.BAD_REQUEST;

        APIException exception = assertThrows(APIException.class, () -> stockShareService.sellStock(stockShareDTO));

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
        assertThat(exception.getStatus()).isEqualTo(exceptionStatus);
    }

    @Test
    void getPortfolio_whenRequestIsForUsersPortfolio_RespondsWithSetOfStocks() {
        StockShareEntity stockShare1 = testUtils.stockShareEntityFactory(UUID.randomUUID().toString(), userID);
        StockShareEntity stockShare2 = testUtils.stockShareEntityFactory(UUID.randomUUID().toString(), userID);

        Set<StockShareEntity> stockShareEntityPortfolio = Set.of(stockShare1, stockShare2);
        given(stockShareDAO.findAllByUserId(userID)).willReturn(stockShareEntityPortfolio);

        //when
        Set<StockShareDTO> stockShareDTOPortfolio = stockShareService.getPortfolio(userID);

        then(stockShareDAO).should().findAllByUserId(userID);
        assertThat(stockShareDTOPortfolio.size()).isEqualTo(2);
    }

    @Test
    void getPortfolio_whenUserPortfolioIsEmpty_RespondsWithEmptySet() {
        Set<StockShareEntity> stockShareEntityPortfolio = Set.of();

        given(stockShareDAO.findAllByUserId(userID)).willReturn(stockShareEntityPortfolio);

        //when
        Set<StockShareDTO> emptyStockSharePortfolio = stockShareService.getPortfolio(userID);

        then(stockShareDAO).should().findAllByUserId(userID);
        assertThat(emptyStockSharePortfolio.size()).isEqualTo(0);
    }
}