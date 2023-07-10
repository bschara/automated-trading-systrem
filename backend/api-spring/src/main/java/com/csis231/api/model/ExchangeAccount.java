package com.csis231.api.model;

import jakarta.persistence.*;

@Table(name = "exchange_account")
@Entity
public class ExchangeAccount{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long account_id;
    @Column(name = "exchange_name", nullable = false)
    private String exchange_name;

    @Column(name = "apikey", nullable = false)
    private String apikey;

    @Column(name = "secretkey")
    private String secretkey;

    @Column(name = "username")
    private String username;



    public ExchangeAccount() {

    }

    public String getExchange_name() {
        return exchange_name;
    }

    public void setExchange_name(String exchange_name) {
        this.exchange_name = exchange_name;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ExchangeAccount(String exchange_name, String apikey, String secretkey, String username){
        super();
        this.exchange_name = exchange_name;
        this.apikey = apikey;
        this.secretkey = secretkey;
        this.username = username;
    }
}

