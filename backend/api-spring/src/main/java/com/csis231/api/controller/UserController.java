package com.csis231.api.controller;

import com.csis231.api.model.ExchangeAccount;
import com.csis231.api.model.Strategy;
import com.csis231.api.model.User;
import com.csis231.api.repository.ExchangeAccountRepository;
import com.csis231.api.service.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeAccountRepository exchangeAccountRepository;

@PostMapping("signup")
public ResponseEntity<User> createUser(@RequestBody User user){
    String email = user.getEmail();
    String username = user.getUserName();
    String password = user.getPassword();

    User savedUser = userService.createUser(email, username, password);
    return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
}

    @PostMapping("login")
    public ResponseEntity<User> loginUser(@RequestParam String username, @RequestParam String password) {
        User user = userService.loginUser(username, password);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/add-account")
    public ResponseEntity<ExchangeAccount> addExchangeAccount(@RequestBody ExchangeAccount exchangeAccount) {
        ExchangeAccount savedExchangeAccount = userService.addExchangeAccount(
                exchangeAccount.getExchange_name(),
                exchangeAccount.getApikey(),
                exchangeAccount.getSecretkey(),
                exchangeAccount.getUsername()
        );
        return new ResponseEntity<>(savedExchangeAccount, HttpStatus.CREATED);
    }
    @GetMapping("/symbols")
    public ResponseEntity<List<String>> getAllSymbols() {
        List<String> symbols = userService.getAllSymbols();
        return new ResponseEntity<>(symbols, HttpStatus.OK);
    }

    @GetMapping("/get-credentials")
    public ResponseEntity<Map<String, String>> getCredentialsByUsername(@RequestParam String username) {
        ExchangeAccount exchangeAccount = userService.getExchangeAccountByUsername(username);
        if (exchangeAccount != null) {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("apikey", exchangeAccount.getApikey());
            credentials.put("secretkey", exchangeAccount.getSecretkey());
            return ResponseEntity.ok(credentials);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-strategy")
    public ResponseEntity<Strategy> createStrategy(@RequestBody Strategy strategy){
       String name = strategy.getName();
       String order_type = strategy.getOrder_type();
       String indicators = strategy.getIndicators();
       String entry_rules = strategy.getEntry_rules();
       String exit_rules = strategy.getExit_rules();

        Strategy savedStrategy = userService.saveStrategy(name, order_type, indicators, entry_rules,exit_rules);
        return new ResponseEntity<Strategy>(savedStrategy, HttpStatus.CREATED);
    }


}


