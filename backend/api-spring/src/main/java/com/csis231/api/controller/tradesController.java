package com.csis231.api.controller;

import com.csis231.api.service.TradesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class tradesController {

    @Autowired
    private TradesService tradesService;

    @PostMapping("/market-order")
    public void handleMarketOrder(@RequestParam("apiKey") String apiKey,
                                  @RequestParam("secretKey") String secretKey,
                                  @RequestParam("symbol") String symbol,
                                  @RequestParam("quantity") String quantity,
                                  @RequestParam("username") String username) {
        tradesService.handleMarketOrder(apiKey, secretKey, symbol, quantity, username);
    }
    @PostMapping("/limit-order")
    public ResponseEntity<String> handleLimitOrder(@RequestParam("apiKey") String apiKey,
                                  @RequestParam("secretKey") String secretKey,
                                  @RequestParam("symbol") String symbol,
                                  @RequestParam("quantity") String quantity,
                                 @RequestParam("price") String price,
                                 @RequestParam("username") String username
                                 ) {
        tradesService.handleLimitOrder(apiKey, secretKey, symbol, quantity,price, username);
        return ResponseEntity.ok("Order handled successfully");
    }

    @PostMapping("/stop-limit-order")
    public ResponseEntity<String> handleStopLimitOrder(@RequestParam("apiKey") String apiKey,
                                                   @RequestParam("secretKey") String secretKey,
                                                   @RequestParam("symbol") String symbol,
                                                   @RequestParam("quantity") String quantity,
                                                   @RequestParam("price") String price,
                                                       @RequestParam("stopLoss") String stopLoss,
                                                       @RequestParam("side") String side,
                                                   @RequestParam("username") String username
    ) {
        tradesService.handleStopOrders(apiKey, secretKey, symbol, quantity,price, stopLoss, side, username);
        return ResponseEntity.ok("Order handled successfully");
    }
    @PostMapping("/cancel-order")
    public void cancelOrder(@RequestParam("apiKey") String apiKey,
                                  @RequestParam("secretKey") String secretKey) {
        tradesService.cancelOrders(apiKey, secretKey);
    }

}
