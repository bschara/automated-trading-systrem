package com.csis231.api.service;


import com.csis231.api.model.ExchangeAccount;
import com.csis231.api.model.Strategy;
import com.csis231.api.model.User;
import com.csis231.api.model.trading_pair;
import com.csis231.api.repository.ExchangeAccountRepository;
import com.csis231.api.repository.PairRepository;
import com.csis231.api.repository.StrategyRepository;
import com.csis231.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExchangeAccountRepository exchangeAccountRepository;

    @Autowired
    private  PairRepository PairRepository;

    @Autowired
    private StrategyRepository strategyRepository;


    public User createUser(String username, String email, String password){
        User user = new User(email,username,password);
        return userRepository.save(user);
    }
    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new IllegalArgumentException("Invalid username");
        }
        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return user;
    }

    public ExchangeAccount addExchangeAccount(String exchangeName, String apiKey, String secretKey, String username) {
        ExchangeAccount exchangeAccount = new ExchangeAccount(exchangeName, apiKey, secretKey, username);
        return exchangeAccountRepository.save(exchangeAccount);
    }

    public List<String> getAllSymbols() {
        List<trading_pair> tradingPairs = (List<trading_pair>) PairRepository.findAll();
        return tradingPairs.stream().map(trading_pair::getSymbol).collect(Collectors.toList());
    }
    public ExchangeAccount getExchangeAccountByUsername(String username) {
        ExchangeAccount exchangeAccount = exchangeAccountRepository.findByUsername(username);
        return exchangeAccount;
    }
    public Strategy saveStrategy(String name, String order_type, String indicators,String entry_rules, String exit_rules ){
        Strategy strategy = new Strategy(name, order_type, indicators, entry_rules, exit_rules);
        return strategyRepository.save(strategy);
    }
}