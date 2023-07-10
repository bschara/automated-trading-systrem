package com.example.trading.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class OrderBookRow {
    private DoubleProperty bidPrice;
    private DoubleProperty bidQuantity;
    private DoubleProperty askPrice;
    private DoubleProperty askQuantity;

    public OrderBookRow(double bidPrice, double bidQuantity, double askPrice, double askQuantity) {
        this.bidPrice = new SimpleDoubleProperty(bidPrice);
        this.bidQuantity = new SimpleDoubleProperty(bidQuantity);
        this.askPrice = new SimpleDoubleProperty(askPrice);
        this.askQuantity = new SimpleDoubleProperty(askQuantity);
    }

    // Getter and Setter methods for bidPrice
    public double getBidPrice() {
        return bidPrice.get();
    }

    public DoubleProperty bidPriceProperty() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice.set(bidPrice);
    }

    // Getter and Setter methods for bidQuantity
    public double getBidQuantity() {
        return bidQuantity.get();
    }

    public DoubleProperty bidQuantityProperty() {
        return bidQuantity;
    }

    public void setBidQuantity(double bidQuantity) {
        this.bidQuantity.set(bidQuantity);
    }

    // Getter and Setter methods for askPrice
    public double getAskPrice() {
        return askPrice.get();
    }

    public DoubleProperty askPriceProperty() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice.set(askPrice);
    }

    // Getter and Setter methods for askQuantity
    public double getAskQuantity() {
        return askQuantity.get();
    }

    public DoubleProperty askQuantityProperty() {
        return askQuantity;
    }

    public void setAskQuantity(double askQuantity) {
        this.askQuantity.set(askQuantity);
    }
}
