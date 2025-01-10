package com.cloudrocker.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudrocker.cache.model.Stock;
import com.cloudrocker.cache.service.StockService;

import io.micrometer.core.annotation.Timed;  

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{ticker}")
    @Timed(description = "Time spent getting stock price", histogram = true)
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStock(ticker);
    }

    @GetMapping("/companyLogo/{ticker}")
    public ResponseEntity<byte[]> getCompanyLogo(@PathVariable String ticker) {
        byte[] imageBytes = stockService.getCompanyLogo(ticker);
        if (imageBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/jpeg"); // Set the appropriate content type
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
} 
