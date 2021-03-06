package com.stocksapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocksapi.DTO.StockShareDTO;
import com.stocksapi.Service.StockShareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StockShareControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    private StockShareDTO stockSharePurchase;
    private StockShareDTO stockShareSale;

    private final String exchange = "NYSE";
    private final String symbol = "MSFT";
    private final String name = "Microsoft";
    private final Double purchaseShares = 2.76;
    private final Double sellShares = 1.01;
    private final String userId = UUID.randomUUID().toString();

    @InjectMocks
    private StockShareController stockShareController;

    @Mock
    private StockShareService stockShareService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(stockShareController).build();

        stockSharePurchase = StockShareDTO.builder()
                .exchange(exchange)
                .symbol(symbol)
                .name(name)
                .shareQuantity(purchaseShares)
            .build();

        stockShareSale = StockShareDTO.builder()
                .exchange(exchange)
                .symbol(symbol)
                .name(name)
                .shareQuantity(sellShares)
            .build();
    }

    @Test
    void purchaseStock_whenRequestForInitialStockShare_RespondsWithResourceAnd201Created() throws Exception {
        given(stockShareService.purchaseStock(any(StockShareDTO.class))).willReturn(stockSharePurchase);

        mockMvc.perform(put("/shares/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockSharePurchase)))
                .andExpect(content().json(objectMapper.writeValueAsString(stockSharePurchase)))
                .andExpect(status().isCreated());

        then(stockShareService).should().purchaseStock(stockSharePurchase);
    }

    @Test
    void purchaseStock_whenRequestForAdditionalStockShares_RespondsWithResourceAnd200OK() throws Exception {
        StockShareDTO additionalStockSharePurchase = StockShareDTO.builder()
                .exchange(exchange)
                .symbol(symbol)
                .name(name)
                .shareQuantity(purchaseShares + purchaseShares)
            .build();
        stockSharePurchase.setId("valid ID");

        given(stockShareService.purchaseStock(stockSharePurchase)).willReturn(additionalStockSharePurchase);

        mockMvc.perform(put("/shares/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockSharePurchase)))
                .andExpect(content().json(objectMapper.writeValueAsString(additionalStockSharePurchase)))
                .andExpect(status().isOk());

        then(stockShareService).should().purchaseStock(stockSharePurchase);
    }

    @Test
    void sellStock_whenRequestForPartialSaleIsReceived_RespondsWithUpdatedResourceAnd200OK() throws Exception {
        StockShareDTO stockShareAfterSale = StockShareDTO.builder()
                .exchange(exchange)
                .symbol(symbol)
                .name(name)
                .shareQuantity(purchaseShares - sellShares)
            .build();

        given(stockShareService.sellStock(stockShareSale)).willReturn(stockShareAfterSale);

        mockMvc.perform(put("/shares/sales")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(stockShareSale)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(stockShareAfterSale)));
    }

    @Test
    void sellStock_whenRequestForFullSaleIsReceived_RespondsWith204NoContent() throws Exception {
        given(stockShareService.sellStock(stockShareSale)).willReturn(null);

        mockMvc.perform(put("/shares/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockShareSale)))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPortfolio_whenRequestForUsersStockPortfolioReceived_RespondsWithListAnd200OK() throws Exception {
        Set<StockShareDTO> portfolio = Set.of(stockSharePurchase, stockShareSale);
        given(stockShareService.getPortfolio(userId)).willReturn(portfolio);

        mockMvc.perform(get("/shares/portfolio/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(portfolio)));
    }

    @Test
    void getPortfolio_whenNoStockSharesExistForUserId_RespondsWith204NoContent() throws Exception {
        Set<StockShareDTO> portfolio = Set.of();
        given(stockShareService.getPortfolio(userId)).willReturn(portfolio);

        mockMvc.perform(get("/shares/portfolio/" + userId))
                .andExpect(status().isNoContent());
    }
}