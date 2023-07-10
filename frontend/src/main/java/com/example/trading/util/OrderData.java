package com.example.trading.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OrderData {
    private  StringProperty symbol;
    private  StringProperty side;
    private  StringProperty quantity;
    private  StringProperty price;

    public OrderData(String symbol, String side, String quantity, String price) {
        this.symbol = new SimpleStringProperty(symbol);
        this.side = new SimpleStringProperty(side);
        this.quantity = new SimpleStringProperty(quantity);
        this.price = new SimpleStringProperty(price);
    }

    public StringProperty symbolProperty() {
        return symbol;
    }

    public StringProperty sideProperty() {
        return side;
    }

    public StringProperty quantityProperty() {
        return quantity;
    }

    public StringProperty priceProperty() {
        return price;
    }
}
