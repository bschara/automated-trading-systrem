package com.example.trading.controllers;



import com.example.trading.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class strategyController{
    Preferences prefs = Preferences.userNodeForPackage(loginController.class);
    String apiKey = prefs.get("apiKey", null);
    String secretKey = prefs.get("secretKey", null);

    String username = prefs.get("username", null);

    @FXML
    private Button backButton4;

   @FXML
   private ComboBox<String> strategyComboBox;
   @FXML
   private ComboBox<String> symbolComboBox;

   @FXML
   private TextField stopLossPercentageTextField;

   @FXML
   private TextField indicatorTextField;

   @FXML TextField quantityField;

    private volatile boolean running = true;

    public void initialize() throws IOException{
        populateComboBox();
    }
    public void autoOrder() throws IOException, InterruptedException {

          if("strat1".equals(strategyComboBox.getValue())){
          String TRADING_PAIR = symbolComboBox.getValue();
          int SMA_PERIOD = Integer.parseInt(indicatorTextField.getText());
          double STOP_LOSS_PERCENTAGE = Double.parseDouble(stopLossPercentageTextField.getText());
          String quantity = quantityField.getText();

     while (running) { // Run indefinitely
            double sma = computeSMA(SMA_PERIOD);
            double currentPrice = getCurrentPrice(TRADING_PAIR);

         if (currentPrice > sma) {
             String side = "BUY";
             BigDecimal entryPrice = BigDecimal.valueOf(currentPrice);
             BigDecimal stopLossPrice = entryPrice.multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(STOP_LOSS_PERCENTAGE)));
             String endpointUrl = "http://localhost:8181/api/limit-order";
             HttpClient httpClient = HttpClient.newHttpClient();
             String requestBody = "apiKey=" + apiKey + "&secretKey=" + secretKey + "&symbol=" + TRADING_PAIR + "&quantity="
                     + quantity + "&price=" + entryPrice + "&stopLoss" + stopLossPrice + "&side" + side + "&username" + username;
             HttpRequest request = HttpRequest.newBuilder()
                     .uri(URI.create(endpointUrl))
                     .header("Content-Type", "application/x-www-form-urlencoded")
                     .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                     .build();
             HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
             int statusCode = response.statusCode();
             String responseBody = response.body();
             HttpHeaders headers = response.headers();
             System.out.println("Status code: " + statusCode);
             System.out.println("Response body: " + responseBody);
             System.out.println("Response headers: " + headers);


         } else if (currentPrice < sma) {
             // Place a limit sell order with a stop loss
             BigDecimal entryPrice = BigDecimal.valueOf(currentPrice);
             BigDecimal stopLossPrice = entryPrice.multiply(BigDecimal.ONE.add(BigDecimal.valueOf(STOP_LOSS_PERCENTAGE)));
             String side = "SELL";
             String endpointUrl = "http://localhost:8181/api/stop-limit-order";
             HttpClient httpClient = HttpClient.newHttpClient();
             String requestBody = "apiKey=" + apiKey + "&secretKey=" + secretKey + "&symbol=" + TRADING_PAIR + "&quantity="
                     + quantity + "&price=" + entryPrice + "&stopLoss" + stopLossPrice + "&side" + side + "&username" + username;
             HttpRequest request = HttpRequest.newBuilder()
                     .uri(URI.create(endpointUrl))
                     .header("Content-Type", "application/x-www-form-urlencoded")
                     .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                     .build();
             HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
             int statusCode = response.statusCode();
             String responseBody = response.body();
             HttpHeaders headers = response.headers();
             System.out.println("Status code: " + statusCode);
             System.out.println("Response body: " + responseBody);
             System.out.println("Response headers: " + headers);
         }



         try {
                Thread.sleep(10000); // Wait for 10 seconds before checking again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }}
    }

    private  double computeSMA(int period) {
        double sma = 0;
        String endpointUrl = "http://localhost:8181/api/get-sma";
        HttpClient httpClient = HttpClient.newHttpClient();
        String requestUrl = endpointUrl + "?period=" + period + "&apiKey=" + apiKey + "&secretKey=" + secretKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                 sma = Double.parseDouble(responseBody);
                System.out.println("SMA: " + sma);
            } else {
                System.out.println("Request failed with status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
      return sma;
    }

    public double getCurrentPrice(String symbol){
        double currentPrice = 0;
        String endpointUrl = "http://localhost:8181/api/current-price";
        HttpClient httpClient = HttpClient.newHttpClient();
        String requestUrl = endpointUrl + "?symbol=" + symbol + "&apiKey=" + apiKey + "&secretKey=" + secretKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                 currentPrice = Double.parseDouble(responseBody);
                System.out.println("Current Price: " + currentPrice);
            } else {
                System.out.println("Request failed with status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    return currentPrice;
    }

    public void handleBackButton4(){
        try {
            //load next view
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/mainView.fxml"));
            Parent mainView = loader.load();
            Scene mainScene = new Scene(mainView);
            Stage primaryStage = (Stage) backButton4.getScene().getWindow();
            primaryStage.setScene(mainScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false; // Set the flag to stop the loop
    }
    private void populateComboBox() {
        String API_URL = "http://localhost:8181/api/symbols";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body(); // Assuming the response body is a JSON array of strings
                JSONArray jsonArray = new JSONArray(responseBody);
                List<String> symbols = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String symbol = jsonArray.getString(i);
                    symbols.add(symbol);
                }
                symbolComboBox.getItems().addAll(symbols);
            } else {
                System.out.println("Request failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

