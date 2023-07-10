package com.example.trading.controllers;


import com.example.trading.Main;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;
import com.example.trading.util.OrderBookRow;
import com.fasterxml.jackson.databind.ObjectMapper;
public class tradingViewController {

    Preferences prefs = Preferences.userNodeForPackage(loginController.class);
    String apiKey = prefs.get("apiKey", null);
    String secretKey = prefs.get("secretKey", null);
    String username = prefs.get("username", null);

    @FXML
    private ComboBox<String> symbolInput;

    @FXML
    private LineChart<String, Double> symbolChart;

    @FXML
    private TableView<OrderBookRow> orderBookTable;

    @FXML
    private TableColumn<OrderBookRow, Double> bidPriceColumn;

    @FXML
    private TableColumn<OrderBookRow, Double> bidQuantityColumn;

    @FXML
    private TableColumn<OrderBookRow, Double> askPriceColumn;

    @FXML
    private TableColumn<OrderBookRow, Double> askQuantityColumn;
    @FXML
    private TextField quantityInput;
    @FXML
    private TextField priceInput;
    @FXML
    private ComboBox orderTypeDropdown;
    @FXML
    private Button backButton;


    private Executor executor = Executors.newSingleThreadExecutor();
    public tradingViewController() throws SQLException, IOException {
    }

    public void initialize() throws IOException {
        populateComboBox();


//        intializeOrderBook();
    }

    public void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/mainView.fxml"));
            Parent mainView = loader.load();
            Scene mainScene = new Scene(mainView);
            Stage primaryStage = (Stage) backButton.getScene().getWindow();
            primaryStage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSymbolSelection() throws IOException {
        intializeOrderBook();
        initializeChart();
    }

    public void initializeChart() throws IOException {
        String selectedSymbol = symbolInput.getValue();
        String endpointUrl = "http://localhost:8181/api/chart-data?vale=" + selectedSymbol;
        executor.execute(() -> {
            try {
                JSONArray chartData = getChartData(endpointUrl);
                // Process the chart data
                XYChart.Series<String, Double> series = new XYChart.Series<>();
                for (int i = 0; i < chartData.length(); i++) {
                    JSONArray data = chartData.getJSONArray(i);
                    long timestamp = data.getLong(0);
                    double closePrice = data.getDouble(4);
                    String dateString = Instant.ofEpochMilli(timestamp)
                            .atZone(ZoneId.systemDefault()).toLocalDate().toString();
                    XYChart.Data<String, Double> dataPoint = new XYChart.Data<>(dateString, closePrice);
                    series.getData().add(dataPoint);
                }
                // Update the LineChart with the new data on the JavaFX application thread
                Platform.runLater(() -> {
                    symbolChart.getData().clear();
                    symbolChart.getData().add(series);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        }

    public void intializeOrderBook() throws IOException {
        String symbol = symbolInput.getValue();
        String endpointUrl = "http://localhost:8181/api/order-book?symbol=" + symbol;
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                // Parse the JSON response manually
                ObjectMapper objectMapper = new ObjectMapper();
                List<List<String>> orderBookData = objectMapper.readValue(responseBody,
                        new TypeReference<List<List<String>>>() {
                        });
                // Clear existing data
                orderBookTable.getItems().clear();
                System.out.println(orderBookData + "\n");
                // Populate the table with the new data
                for (List<String> row : orderBookData) {
                    if (row.size() == 4) {
                        double bidPrice = Double.parseDouble(row.get(0));
                        double bidQuantity = Double.parseDouble(row.get(1));
                        double askPrice = Double.parseDouble(row.get(2));
                        double askQuantity = Double.parseDouble(row.get(3));
                        System.out.println(bidPrice);

                        // Make sure the data types match the OrderBookRow class
                        OrderBookRow orderBookRow = new OrderBookRow(
                                bidPrice,                // double
                                bidQuantity,             // double
                                askPrice,                // double
                                askQuantity              // double
                        );
                        orderBookTable.getItems().add(orderBookRow);
                    }
                }
                bidPriceColumn.setCellValueFactory(new PropertyValueFactory<>("bidPrice"));
                bidQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("bidQuantity"));
                askPriceColumn.setCellValueFactory(new PropertyValueFactory<>("askPrice"));
                askQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("askQuantity"));
            } else {
                System.out.println("Request failed with status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
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
                symbolInput.getItems().addAll(symbols);
            } else {
                System.out.println("Request failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleOrders() throws Exception {
        String symbol = priceInput.getText();
        String quantity = quantityInput.getText();
        String price = priceInput.getText();


        if("LIMIT".equals(orderTypeDropdown.getValue())){
            String endpointUrl = "http://localhost:8181/api/limit-order";
            HttpClient httpClient = HttpClient.newHttpClient();
            String requestBody = "apiKey=" + apiKey + "&secretKey=" + secretKey + "&symbol=" + symbol + "&quantity=" + quantity + "&price=" + price + "&username" + username;
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


    else if("Market".equals(orderTypeDropdown.getValue())){
            String endpointUrl = "http://localhost:8181/api/market-order";
            HttpClient httpClient = HttpClient.newHttpClient();
            String requestBody = "apiKey=" + apiKey + "&secretKey=" + secretKey + "&symbol=" + symbol + "&quantity=" + quantity + "&username" + username;
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
    }

    private JSONArray getChartData(String endpointUrl) throws IOException, InterruptedException, JSONException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            String responseBody = response.body();
            return new JSONArray(responseBody);
        } else {
            throw new RuntimeException("Request failed with status code: " + response.statusCode());
        }
    }

}





