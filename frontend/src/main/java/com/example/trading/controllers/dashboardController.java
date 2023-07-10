package com.example.trading.controllers;

import com.example.trading.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import com.example.trading.util.OrderData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class dashboardController {
    Preferences prefs = Preferences.userNodeForPackage(loginController.class);
    String apiKey = prefs.get("apiKey", null);
    String secretKey = prefs.get("secretKey", null);
    @FXML
    private TableView<OrderData> ordersTable;
    @FXML
    private TableColumn<OrderData, String> symbolColumn;
    @FXML
    private TableColumn<OrderData, String> sideColumn;
    @FXML
    private TableColumn<OrderData, String> quantityColumn;
    @FXML
    private TableColumn<OrderData, String> priceColumn;

    @FXML
    private Label fiatValueLabel;
    @FXML
    private Label spotValueLabel;
    @FXML
    private Label estimatedValueLabel;

    @FXML
    private Button backButton3;

    public void initialize() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        getUserData();
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        sideColumn.setCellValueFactory(new PropertyValueFactory<>("side"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        getOpenOrders();
    }

    public void handleBackButton3(){
        try {
            //load next view
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/mainView.fxml"));
            Parent mainView = loader.load();
            Scene mainScene = new Scene(mainView);
            Stage primaryStage = (Stage) backButton3.getScene().getWindow();
            primaryStage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getUserData() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String API_URL = "http://localhost:8181/api/account-values";
        String requestBody = "?apiKey=" + apiKey + "&secretKey=" + secretKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + requestBody))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAcceptAsync(responseBody -> {
                    try {
                        JSONObject responseJson = new JSONObject(responseBody);
                        double totalValue = responseJson.getDouble("totalValue");
                        double spotValue = responseJson.getDouble("spotValue");
                        double fiatValue = responseJson.getDouble("fiatValue");
                        Platform.runLater(() -> {
                            estimatedValueLabel.setText(String.valueOf(totalValue));
                            spotValueLabel.setText(String.valueOf(spotValue));
                            fiatValueLabel.setText(String.valueOf(fiatValue));
                        });
                    } catch (JSONException e) {
                        Platform.runLater(() -> showError("Error parsing response: " + e.getMessage()));
                    }
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> showError("Error fetching account values: " + throwable.getMessage()));
                    return null;
                });
    }
    public void getOpenOrders() {
        String API_URL = "http://localhost:8181/api/open-positions";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?apiKey=" + apiKey + "&secretKey=" + secretKey))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    // Process the response body here
                    List<OrderData> orderDataList = parseOrderData(responseBody);
                    Platform.runLater(() -> ordersTable.getItems().addAll(orderDataList));
                })
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }
    public void cancelOrders() {
        String API_URL = "http://localhost:8181/api/cancel-order";;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("apiKey=" + apiKey + "&secretKey=" + secretKey))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    // Process the response here
                    if (response.statusCode() == 200) {
                        Platform.runLater(() -> {
                            showError("order canceled successfully");
                        });
                    } else {
                        Platform.runLater(() -> showError("Error canceling orders: " + response.body()));
                    }
                })
                .exceptionally(throwable -> {
                    // Handle any exceptions here
                    throwable.printStackTrace();
                    return null;
                });
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private List<OrderData> parseOrderData(String responseBody) {
        List<OrderData> orderDataList = new ArrayList<>();

        try {
            JSONArray ordersArray = new JSONArray(responseBody);
            for (int i = 0; i < ordersArray.length(); i++) {
                JSONObject orderJson = ordersArray.getJSONObject(i);
                String symbol = orderJson.getString("symbol");
                String side = orderJson.getString("side");
                String quantity = orderJson.getString("quantity");
                String price = orderJson.getString("price");

                OrderData orderData = new OrderData(symbol, side, quantity, price);
                orderDataList.add(orderData);
            }
        } catch (JSONException e) {
            Platform.runLater(() -> showError("Error parsing response: " + e.getMessage()));
        }

        return orderDataList;
    }

}
