package com.cloudrocker.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;  

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/{symbol}")
    @Timed(description = "Time spent getting stock price", histogram = true)
    public String getStock(@PathVariable String symbol) {
        try {
            String quote = (String) redisTemplate.opsForValue().get("k8s-caching:" + symbol);
            if (quote == null) {
                quote = "$100.23";
                redisTemplate.opsForValue().setIfAbsent("k8s-caching:" + symbol, quote);
            }
            return "Connected to Redis successfully! The price of stock" + symbol + " is: " + quote;
        } catch (Exception e) {
            return "Failed to connect to Redis: " + e.getMessage();
        }
    }
} 