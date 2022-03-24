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

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StockShareDAOTest {

    @Autowired
    private StockShareDAO stockShareDAO;

    @Autowired
    private TestEntityManager entityManager;

    TestUtils testUtils = new TestUtils();

    private String validUserId;

    @BeforeEach
    void setup() {
        UserEntity user = UserEntity.builder().id(validUserId).build();
        user = entityManager.persist(user);
        validUserId = user.getId();

        StockShareEntity stockShare1 = testUtils.stockShareEntityFactory(null, null);
        stockShare1.setUser(user);
        StockShareEntity stockShare2 = testUtils.stockShareEntityFactory(null, null);
        stockShare2.setUser(user);
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
    void findAllByUserId_whenGivenInvalidId_ThrowsException() {
        String invalidUserId = "invalidUserId";
        Set<StockShareEntity> stockShares = stockShareDAO.findAllByUserId(invalidUserId);

        assertThat(stockShares.size()).isEqualTo(0);
    }
}