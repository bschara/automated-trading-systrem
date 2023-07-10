package com.csis231.api.model;

import jakarta.persistence.*;

@Table(name = "trading_history")
@Entity
public class Trades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long trade_id;
    @Column(name = "trading_time", nullable = false)
    private String trading_time;

    @Column(name = "order_type", nullable = false)
    private String order_type;

    @Column(name = "order_price")
    private String order_price;

   @Column(name = "order_quantity")
   private String order_quantity;

   @Column(name = "trading_status")
   private String trade_status;

   @Column(name = "symbol")
   private String symbol;

   @Column(name = "username")
   private String username;



    public Trades() {

    }

    public Trades(String trading_time, String order_type, String order_price, String order_quantity,
                  String trade_status, String symbol, String username) {
        super();
        this.trading_time = trading_time;
        this.order_type = order_type;
        this.order_price = order_price;
        this.order_quantity = order_quantity;
        this.trade_status = trade_status;
        this.symbol = symbol;
        this.username = username;
    }
    public long getId() {
        return trade_id;
    }
    public void setId(long id) {
        this.trade_id = id;
    }

    public String getTrading_time() {
        return trading_time;
    }

    public void setTrading_time(String trading_time) {
        this.trading_time = trading_time;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(String order_quantity) {
        this.order_quantity = order_quantity;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}