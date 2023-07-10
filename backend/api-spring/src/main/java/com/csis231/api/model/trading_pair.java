package com.csis231.api.model;


import jakarta.persistence.*;

@Table(name = "trading_pair")
@Entity
public class trading_pair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pair_id;

    @Column(name = "symbol", nullable = false)
    private String symbol;
    @Column(name = "base_currency", nullable = false)
    private String base_currency;
    @Column(name = "quote_currency", nullable = false)
    private String quote_currency;




    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBase_currency() {
        return base_currency;
    }

    public void setBase_currency(String base_currency) {
        this.base_currency = base_currency;
    }

    public String getQuote_currency() {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency) {
        this.quote_currency = quote_currency;
    }
}
