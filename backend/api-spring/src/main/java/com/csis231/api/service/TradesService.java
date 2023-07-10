package com.csis231.api.service;

import com.binance.api.client.domain.account.NewOrderResponseType;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.csis231.api.model.Trades;
import com.csis231.api.repository.TradeRepository;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.OrderType;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.exception.BinanceApiException;
import java.util.List;

@Service
public class TradesService {

    @Autowired
    private TradeRepository tradesrepo;

    public void handleMarketOrder(String apiKey,String secretKey, String symbol, String quantity, String username){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();
            // Set up the order parameters
            OrderSide side = OrderSide.BUY;
            OrderType type = OrderType.MARKET;
            long serverTime = client.getServerTime();
            String updatedServerTime = Long.toString(serverTime);
            // Create the new order request
            NewOrder order = new NewOrder(symbol, side, type, null, quantity);
            try {
                // Send the order to Binance
                NewOrderResponse response = client.newOrder(order);
                // Get the order details
                OrderStatusRequest orderStatusRequest = new OrderStatusRequest(symbol, response.getOrderId());
                Order orderDetails = (Order) client.getOrderStatus(orderStatusRequest);
                // Print the order details
                System.out.println("Order placed successfully: " + orderDetails);
                Trades trade = new Trades();
                trade.setTrading_time(updatedServerTime);
                trade.setOrder_type(type.toString());
                trade.setOrder_price("");
                trade.setOrder_quantity(quantity);
                trade.setTrade_status("successful");
                trade.setSymbol(symbol);
                trade.setUsername(username);
                tradesrepo.save(trade);
            } catch (BinanceApiException e) {
                // Handle any errors
                Trades trade = new Trades();
                trade.setTrading_time(updatedServerTime);
                trade.setOrder_type(type.toString());
                trade.setOrder_price("");
                trade.setOrder_quantity(quantity);
                trade.setTrade_status("failed");
                trade.setSymbol(symbol);
                trade.setUsername(username);
                tradesrepo.save(trade);
                System.out.println("Error placing order: " + e.getError().getCode() + ", " + e.getError().getMsg());
            }
        }

    public void handleLimitOrder(String apiKey, String secretKey, String symbol, String quantity,
                                 String price, String username) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();
        OrderSide side = OrderSide.BUY;
        OrderType type = OrderType.LIMIT;
        long serverTime = client.getServerTime();
        String updatedServerTime = Long.toString(serverTime);
        TimeInForce timeInForce = TimeInForce.GTC;
        // Create the new order request
        NewOrder order = new NewOrder(symbol, side, type, timeInForce, quantity, price);
        System.out.println(order);
        try {
            // Send the order to Binance
            NewOrderResponse response = client.newOrder(order);
            // Get the order details
            OrderStatusRequest orderStatusRequest = new OrderStatusRequest(symbol, response.getOrderId());
            com.binance.api.client.domain.account.Order orderDetails = client.getOrderStatus(orderStatusRequest);
            // Print the order details
            System.out.println("Order placed successfully: " + orderDetails);
            Trades trade = new Trades();
            trade.setTrading_time(updatedServerTime);
            trade.setOrder_type(type.toString());
            trade.setOrder_price(price);
            trade.setOrder_quantity(quantity);
            trade.setTrade_status("successful");
            trade.setSymbol(symbol);
            trade.setUsername(username);
            tradesrepo.save(trade);
        } catch (BinanceApiException e) {
            // Handle any errors
            Trades trade = new Trades();
            trade.setTrading_time(updatedServerTime);
            trade.setOrder_type(type.toString());
            trade.setOrder_price(price);
            trade.setOrder_quantity(quantity);
            trade.setTrade_status("failed");
            trade.setSymbol(symbol);
            trade.setUsername(username);
            tradesrepo.save(trade);
            System.out.println("Error placing order: " + e.getError().getCode() + ", " + e.getError().getMsg());
        }
    }

    public void handleStopOrders(String apiKey, String secretKey, String symbol, String quantity,
                                 String price, String stopLoss,String side,String username){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();
        OrderSide orderSide = OrderSide.valueOf(side);
        OrderType type = OrderType.STOP_LOSS_LIMIT;
        long serverTime = client.getServerTime();
        String updatedServerTime = Long.toString(serverTime);
//        TimeInForce timeInForce = TimeInForce.GTC;
        // Create the new order request

        try {
            // Send the order to Binance
            NewOrderResponse response = client.newOrder(
                    new NewOrder(symbol, orderSide, OrderType.STOP_LOSS_LIMIT, TimeInForce.GTC, quantity)
                            .price(price)
                            .stopPrice(stopLoss)
                            .newOrderRespType(NewOrderResponseType.FULL));
            // Get the order details
            OrderStatusRequest orderStatusRequest = new OrderStatusRequest(symbol, response.getOrderId());
            com.binance.api.client.domain.account.Order orderDetails = client.getOrderStatus(orderStatusRequest);
            // Print the order details
            System.out.println("Order placed successfully: " + orderDetails);
            Trades trade = new Trades();
            trade.setTrading_time(updatedServerTime);
            trade.setOrder_type(type.toString());
            trade.setOrder_price(price);
            trade.setOrder_quantity(quantity);
            trade.setTrade_status("successful");
            trade.setSymbol(symbol);
            trade.setUsername(username);
            tradesrepo.save(trade);
        } catch (BinanceApiException e) {
            // Handle any errors
            Trades trade = new Trades();
            trade.setTrading_time(updatedServerTime);
            trade.setOrder_type(type.toString());
            trade.setOrder_price(price);
            trade.setOrder_quantity(quantity);
            trade.setTrade_status("failed");
            trade.setSymbol(symbol);
            trade.setUsername(username);
            tradesrepo.save(trade);
            System.out.println("Error placing order: " + e.getError().getCode() + ", " + e.getError().getMsg());
        }

    }
    public void cancelOrders(String apiKey, String secretKey){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);

        BinanceApiRestClient client = factory.newRestClient();

        List<com.binance.api.client.domain.account.Order> openOrders = client.getOpenOrders(new OrderRequest(null));

        for (com.binance.api.client.domain.account.Order order : openOrders) {
            client.cancelOrder(new CancelOrderRequest(order.getSymbol(), order.getOrderId()));
        }
    }

}



