package com.csis231.api.controller;

import com.csis231.api.service.UserInfoService;
import com.csis231.api.utils.AccountValues;
import com.csis231.api.utils.OrderData;
import com.csis231.api.utils.TradeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @GetMapping("/account-values")
    public ResponseEntity<AccountValues> getAccountValues(
            @RequestParam("apiKey") String apiKey,
            @RequestParam("secretKey") String secretKey
    ) {
        AccountValues accountValues = userInfoService.getAccountValues(apiKey, secretKey);
        return new ResponseEntity<>(accountValues, HttpStatus.OK);
    }
    @GetMapping("/trade-data")
    public ResponseEntity<List<TradeData>> getTradeData(String apiKey, String secretKey) {
        List<TradeData> tradeData = userInfoService.getTradeData(apiKey, secretKey);
        return new ResponseEntity<>(tradeData, HttpStatus.OK);
    }

    @GetMapping("/open-positions")
    public ResponseEntity<List<OrderData>> getOpenPositions(
            @RequestParam("apiKey") String apiKey,
            @RequestParam("secretKey") String secretKey
    ) {
        List<OrderData> openPositions = userInfoService.getOpenPositions(apiKey, secretKey);
        return new ResponseEntity<>(openPositions, HttpStatus.OK);
    }

}
