package com.cloudrocker.cache.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cloudrocker.cache.model.Stock;

@Service
public class StockService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Stock getStock(String ticker) {
       String cacheKey = "k8s-caching:stock:" + ticker;
        String quote = (String) redisTemplate.opsForValue().get(cacheKey);
        if (quote == null) {
            quote = "$100.23";
            redisTemplate.opsForValue().setIfAbsent(cacheKey, quote);
        }
        Stock stock = new Stock();
        stock.setTicker(ticker);
        stock.setQuote(quote);
        return stock;
    }

    public byte[] getCompanyLogo(String ticker) {
        // Retrieve the image from Redis
        String cacheKey = "k8s-caching:stock:logo:" + ticker;
        byte[] imageBytes = (byte[]) redisTemplate.opsForValue().get(cacheKey);
        if (imageBytes == null) {
            try {
                Path path = Paths.get("/workspaces/k8s-caching/src/main/resources/logo.png");
                imageBytes = Files.readAllBytes(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            if (imageBytes != null) {
                // Store the image in Redis
                redisTemplate.opsForValue().set(cacheKey, imageBytes);
            }
        }
        return imageBytes;
    }

    public void flushAllDatabases() {
        var factory = redisTemplate.getConnectionFactory();
        if (factory != null) {
            factory.getConnection().flushAll();
        } else {
            throw new IllegalStateException("Redis connection factory not initialized");
        }
    }
}
 