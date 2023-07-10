package com.example.trading.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public  class TradeData {
        private final StringProperty symbol;
        private final DoubleProperty price;
        private final DoubleProperty quantity;
        private final DoubleProperty totalPrice;

        public TradeData(String symbol, double price, double quantity, double totalPrice) {
            this.symbol = new SimpleStringProperty(symbol);
            this.price = new SimpleDoubleProperty(price);
            this.quantity = new SimpleDoubleProperty(quantity);
            this.totalPrice = new SimpleDoubleProperty(totalPrice);
        }

        public StringProperty symbolProperty() {
            return symbol;
        }

        public DoubleProperty priceProperty() {
            return price;
        }

        public DoubleProperty quantityProperty() {
            return quantity;
        }

        public DoubleProperty totalPriceProperty() {
            return totalPrice;
        }
    }