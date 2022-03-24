package com.stocksapi.Controller;

import com.stocksapi.DTO.StockShareDTO;
import com.stocksapi.Service.StockShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shares")
public class StockShareController {

    private final StockShareService stockShareService;

    @PutMapping( value = "/purchases", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockShareDTO> purchaseStock(@RequestBody StockShareDTO stockShare) {
        StockShareDTO savedStockShare = stockShareService.purchaseStock(stockShare);
        return new ResponseEntity<>(
                savedStockShare,
                stockShare.getId() != null ? HttpStatus.OK : HttpStatus.CREATED
        );
    }

    @PutMapping(value = "/sales", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockShareDTO> sellStock(@RequestBody StockShareDTO stockShareSale) {
        StockShareDTO remainingStockShare = stockShareService.sellStock(stockShareSale);
        if (remainingStockShare == null)
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return  new ResponseEntity<>(remainingStockShare, HttpStatus.OK);
    }

    @GetMapping("/portfolio/{userID}")
    public ResponseEntity<Set<StockShareDTO>> getPortfolio(@PathVariable String userID) {
        Set<StockShareDTO> portfolio = stockShareService.getPortfolio(userID);
        if (portfolio.size() == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(portfolio, HttpStatus.OK);
    }
}
