package com.csis231.api.service;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.OrderStatus;
import com.binance.api.client.domain.OrderType;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.exception.BinanceApiException;
import com.csis231.api.utils.AccountValues;
import com.csis231.api.utils.OrderData;
import com.csis231.api.utils.TradeData;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoService {

    public List<TradeData> getTradeData(String apiKey, String secretKey) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();

        List<AssetBalance> balances = client.getAccount().getBalances();

        List<TradeData> data = new ArrayList<>();

        for (AssetBalance balance : balances) {
            String asset = balance.getAsset();
            String free = balance.getFree();
            BigDecimal newFree = new BigDecimal(free);
            String locked = balance.getLocked();
            BigDecimal newLocked = new BigDecimal(locked);
            BigDecimal total = newFree.add(newLocked);

            if (total.compareTo(BigDecimal.ZERO) > 0) {
                String symbol = asset + "USDT";
                try {
                    TickerPrice tickerPrice = client.getPrice(symbol);
                    BigDecimal price = new BigDecimal(tickerPrice.getPrice());

                    BigDecimal quantity = new BigDecimal(balance.getFree());
                    BigDecimal totalPrice = price.multiply(quantity);
                    data.add(new TradeData(symbol, price.doubleValue(), quantity.doubleValue(), totalPrice.doubleValue()));
                } catch (BinanceApiException e) {
                    System.err.println("Error getting trades for symbol " + symbol + ": " + e.getMessage());
                }
            }
        }

        return data;
    }

    public List<OrderData> getOpenPositions(String apiKey, String secretKey) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();

        List<Order> orders = client.getOpenOrders(new OrderRequest(null));

        List<OrderData> openPositions = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.NEW &&
                    order.getType() == OrderType.LIMIT &&
                    order.getTimeInForce() == TimeInForce.GTC) {
                OrderData openPosition = new OrderData(order.getSymbol(), order.getSide().toString(),
                        order.getOrigQty().toString(), order.getPrice().toString());
                openPositions.add(openPosition);
                System.out.println(openPosition);
            }
        }

        return openPositions;
    }
    public AccountValues getAccountValues(String apiKey, String secretKey) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();
        Account account = client.getAccount();

        BigDecimal totalValue = BigDecimal.ZERO;

        // Calculate total value
        for (AssetBalance balance : account.getBalances()) {
            String asset = balance.getAsset();
            if (!asset.equals("USDT")) {
                String free = balance.getFree();
                BigDecimal newFree = new BigDecimal(free);
                String locked = balance.getLocked();
                BigDecimal newLocked = new BigDecimal(locked);
                BigDecimal total = newFree.add(newLocked);

                if (total.compareTo(BigDecimal.ZERO) > 0) {
                    String symbol = asset + "USDT";

                    TickerPrice tickerPrice = client.getPrice(symbol);
                    BigDecimal price = new BigDecimal(tickerPrice.getPrice());

                    BigDecimal quantity = new BigDecimal(balance.getFree());
                    BigDecimal totalPrice = price.multiply(quantity);
                    totalValue = totalValue.add(totalPrice);
                }
            }
        }
        // Get spot value
        String spotValue = account.getAssetBalance("USDT").getFree();
        BigDecimal newSpotValue = new BigDecimal(spotValue);
        // Get fiat value
        String fiatValue = account.getAssetBalance("USD").getFree();
        BigDecimal newFiatValue = new BigDecimal(fiatValue);

        return new AccountValues(totalValue, newSpotValue, newFiatValue);
    }
}
