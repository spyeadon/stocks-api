package com.stocksapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocksapi.DTO.StockShareDTO;
import com.stocksapi.Service.StockShareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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
    void purchaseStock_whenRequestReceived_RespondsWithResourceAnd201Created() throws Exception {
        doReturn(stockSharePurchase).when(stockShareService).purchaseStock(any(StockShareDTO.class));

        mockMvc.perform(put("/shares/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockSharePurchase)))
                .andExpect(content().json(objectMapper.writeValueAsString(stockSharePurchase)))
                .andExpect(status().isCreated());
    }

    @Disabled
    @Test
    void sellStock_whenRequestForPartialSaleIsReceived_RespondsWithUpdatedResourceAnd200OK() throws Exception {
        StockShareDTO stockShareAfterSale = StockShareDTO.builder()
                .exchange(exchange)
                .symbol(symbol)
                .name(name)
                .shareQuantity(purchaseShares - sellShares)
            .build();

        doReturn(stockShareAfterSale).when(stockShareService).sellStock(stockShareSale);

        mockMvc.perform(put("/shares/sales")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(stockShareSale)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(stockShareAfterSale)));
    }

    @Disabled
    @Test
    void sellStock_whenRequestForFullSaleIsReceived_RespondsWith204NoContent() {
        StockShareDTO stockShareAfterSale = StockShareDTO.builder()
                .exchange(exchange)
                .symbol(symbol)
                .name(name)
                .shareQuantity(0.0)
                .build();

        doReturn(stockShareAfterSale).when(stockShareService).sellStock(stockShareSale);

    }
}