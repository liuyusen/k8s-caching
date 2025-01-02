package com.cloudrocker.cache;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{symbol}")
    @Timed(description = "Time spent getting stock price", histogram = true)
    public Stock getStock(@PathVariable String symbol) {
        return stockService.getStock(symbol);
    }
} 
