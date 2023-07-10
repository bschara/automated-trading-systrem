package com.csis231.api.service;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarketDataService {

public List<Object[]> initializeChart(String newValue) throws IOException {
        String endpoint = "https://api.binance.com/api/v3/klines?symbol=" + newValue + "&interval=1d";
        System.out.println(endpoint);
        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Read the response into a string
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Print the response for debugging
        System.out.println("Response: " + response.toString());
        System.out.println("\n\n");

        // Parse the response using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object[]> responseList = objectMapper.readValue(response.toString(), List.class);

        return responseList;
    }

    public List<List<String>> initializeOrderBook(String symbol) {
        List<List<String>> result = new ArrayList<>();

        String endpoint = "https://api.binance.com/api/v3/depth?symbol=" + symbol + "&limit=10";
        // Make the API request
        URL url = null;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            throw new RuntimeException(ex);
        }
        try {
            int responseCode = con.getResponseCode();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        // Read the response into a string
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        String inputLine;
        StringBuilder response = new StringBuilder();
        while (true) {
            try {
                if (!((inputLine = in.readLine()) != null))
                    break;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            response.append(inputLine);
        }
        try {
            in.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        // Parse the response into a JSONObject
        JSONObject responseJson = new JSONObject(response.toString());
        // Extract the bid and ask arrays from the JSONObject
        JSONArray bidArray = responseJson.getJSONArray("bids");
        JSONArray askArray = responseJson.getJSONArray("asks");

        // Convert the bid and ask arrays to List<String> and add them to the result list
        List<String> bidList = new ArrayList<>();
        for (int i = 0; i < bidArray.length(); i++) {
            JSONArray bid = bidArray.getJSONArray(i);
            String price = bid.getString(0);
            String quantity = bid.getString(1);
            bidList.add("Price: " + price + ", Quantity: " + quantity);
        }
        result.add(bidList);

        List<String> askList = new ArrayList<>();
        for (int i = 0; i < askArray.length(); i++) {
            JSONArray ask = askArray.getJSONArray(i);
            String price = ask.getString(0);
            String quantity = ask.getString(1);
            askList.add("Price: " + price + ", Quantity: " + quantity);
        }
        result.add(askList);

        return result;
    }

    public List<String> getNews() throws IOException {
        // Populate the table with data from the API
        final String apiKey = "2acd4f444dmsh33e712a7f9fdb1cp1192b8jsn27846e612f1c";
        final String apiHost = "crypto-news16.p.rapidapi.com";
        final String apiUrl = "https://crypto-news16.p.rapidapi.com/news/top/13";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("X-RapidAPI-Key", apiKey)
                .addHeader("X-RapidAPI-Host", apiHost)
                .build();

        Response response = client.newCall(request).execute();
        String jsonResponse = response.body().string();
        System.out.println(jsonResponse);

        // Parse the JSON response into a List<String>
        JSONArray newsArray = new JSONArray(jsonResponse);
        List<String> newsList = new ArrayList<>();
        for (int i = 0; i < newsArray.length(); i++) {
            JSONObject newsObject = newsArray.getJSONObject(i);
            String newsItem = newsObject.getString("title");
            newsList.add(newsItem);
        }

        return newsList;
    }

    public  double computeSMA( int period, String apiKey, String secretKey) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();
        List<TickerPrice> prices = client.getAllPrices();
        if (prices.size() < period) {
            throw new IllegalArgumentException("Not enough data to compute SMA");
        }

        double sum = 0;
        for (int i = prices.size() - period; i < prices.size(); i++) {
            sum += Double.parseDouble(prices.get(i).getPrice());
        }

        return sum / period;
    }

    public double getCurrentPrice(String apiKey, String secretKey, String symbol) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secretKey);
        BinanceApiRestClient client = factory.newRestClient();

        TickerPrice tickerPrice = client.getPrice(symbol);
        double currentPrice = Double.parseDouble(tickerPrice.getPrice());
   return currentPrice;
}
}
