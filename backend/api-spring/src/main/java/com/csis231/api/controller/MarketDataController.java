package com.csis231.api.controller;

import com.csis231.api.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class MarketDataController {
    @Autowired
    private MarketDataService marketDataService;
    @GetMapping("/chart-data")
    public List initializeChart(@RequestParam("vale") String newValue) throws IOException {
        List array = marketDataService.initializeChart(newValue);
        return array;
    }
    @GetMapping("/order-book")
    public List<List<String>> getOrderBook(@RequestParam("symbol") String symbol) {
        List<List<String>> orderBook = marketDataService.initializeOrderBook(symbol);
        return orderBook;
    }
    @GetMapping("/news-data")
    public List<String> getNews() throws IOException {
        return marketDataService.getNews();
    }
    @GetMapping("/current-price")
    public double getCurrentPrice(@RequestParam("symbol") String symbol,
                                  @RequestParam("apiKey") String apiKey,
                                  @RequestParam("secretKey") String secretKey
                                  ) throws IOException {
        return marketDataService.getCurrentPrice(apiKey,secretKey,symbol);
    }
    @GetMapping("/get-sma")
    public double computeSma( @RequestParam("period")  int period,
                              @RequestParam("apiKey") String apiKey,
                             @RequestParam("secretKey") String secretKey
                            ) throws IOException {
        return marketDataService.computeSMA(period,apiKey,secretKey);
    }

}
