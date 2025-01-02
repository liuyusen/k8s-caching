package com.cloudrocker.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cloudrocker.cache.model.Stock;

@Service
public class StockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Stock getStock(String symbol) {
        String cacheKey = "k8s-caching:stock:" + symbol;
        String quote = (String) redisTemplate.opsForValue().get(cacheKey);
        if (quote == null) {
            quote = "$100.23";
            redisTemplate.opsForValue().setIfAbsent(cacheKey, quote);
        }
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setQuote(quote);
        return stock;
    }
}
