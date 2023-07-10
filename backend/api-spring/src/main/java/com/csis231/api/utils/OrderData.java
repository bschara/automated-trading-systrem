package com.csis231.api.utils;

public class OrderData {
    private String symbol;
    private String side;
    private String quantity;
    private String price;

    public OrderData(String symbol, String side, String quantity, String price) {
        this.symbol = symbol;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
