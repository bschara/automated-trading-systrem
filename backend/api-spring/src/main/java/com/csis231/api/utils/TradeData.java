package com.csis231.api.utils;

public class TradeData {
    private final String symbol;
    private final double price;
    private final double quantity;
    private final double totalPrice;

    public TradeData(String symbol, double price, double quantity, double totalPrice) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

