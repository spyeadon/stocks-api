package com.stocksapi.Persistence.DAO;

import com.stocksapi.Persistence.Entity.StockShareEntity;
import com.stocksapi.Persistence.Entity.UserEntity;
import com.stocksapi.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StockShareDAOTest {

    @Autowired
    private StockShareDAO stockShareDAO;

    @Autowired
    private TestEntityManager entityManager;

    TestUtils testUtils = new TestUtils();

    private String validUserId;
    private String invalidUserId;
    private String symbol;

    @BeforeEach
    void setup() {
        UserEntity user = UserEntity.builder().id(validUserId).build();
        user = entityManager.persist(user);
        validUserId = user.getId();
        invalidUserId = "invalidUserId";

        symbol = testUtils.getSymbol();

        StockShareEntity stockShare1 = testUtils.stockShareEntityFactory(null, null);
        stockShare1.setUser(user);
        StockShareEntity stockShare2 = testUtils.stockShareEntityFactory(null, null);
        stockShare2.setUser(user);
        stockShare2.setSymbol("APPL");
        entityManager.persist(stockShare1);
        entityManager.persist(stockShare2);
    }

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

    @Test
    void findAllByUserId_whenGivenValidId_RespondsWithStockShareSet() {
        Set<StockShareEntity> stockShares = stockShareDAO.findAllByUserId(validUserId);

        assertThat(stockShares.size()).isEqualTo(2);
    }

    @Test
    void findAllByUserId_whenGivenInvalidId_RespondsWithEmptyStockShareSet() {
        Set<StockShareEntity> stockShares = stockShareDAO.findAllByUserId(invalidUserId);

        assertThat(stockShares.size()).isEqualTo(0);
    }

    @Test
    void getBySymbolAndUserId_withValidInput_RespondsWithStockShare() {
        Optional<StockShareEntity> stockShareOptional = stockShareDAO.getBySymbolAndUserId(symbol, validUserId);

        assertTrue(stockShareOptional.isPresent());
        StockShareEntity stockShare = stockShareOptional.get();
        assertThat(stockShare.getSymbol()).isEqualTo(symbol);
        assertThat(stockShare.getUser().getId()).isEqualTo(validUserId);
    }

    @Test
    void getBySymbolAndUserId_withInvalidInput_RespondsWithEmptyOptional() {
        Optional<StockShareEntity> stockShareOptional = stockShareDAO.getBySymbolAndUserId(symbol, invalidUserId);

        assertTrue(stockShareOptional.isEmpty());
    }
}