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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StockShareControllerTest {

    private MockMvc mockMvc;
    private StockShareDTO stockShareRequest;
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private StockShareController stockShareController;

    @Mock
    private StockShareService stockShareService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(stockShareController).build();

        stockShareRequest = StockShareDTO.builder()
                .exchange("NYSE")
                .symbol("MSFT")
                .name("Microsoft")
                .shareQuantity(2.76)
                .build();
    }

    @Test
    void purchaseStock_whenRequestReceived_RespondsWithResourceAnd201Created() throws Exception {
        doReturn(stockShareRequest).when(stockShareService).purchaseStock(any(StockShareDTO.class));

        mockMvc.perform(put("/shares/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockShareRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(stockShareRequest)))
                .andExpect(status().isCreated());
    }
}