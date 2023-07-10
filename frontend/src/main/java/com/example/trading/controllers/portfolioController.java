package com.example.trading.controllers;

import com.example.trading.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.example.trading.util.TradeData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.prefs.Preferences;

public class portfolioController {

    Preferences prefs = Preferences.userNodeForPackage(loginController.class);
    String apiKey = prefs.get("apiKey", null);
    String secretKey = prefs.get("secretKey", null);

    @FXML
    private Button backButton2;
    @FXML
    private TableView<TradeData> balancesTable;
    @FXML
    private TableColumn<TradeData, String> symbolColumn;
    @FXML
    private TableColumn<TradeData, Double> priceColumn;
    @FXML
    private TableColumn<TradeData, Double> quantityColumn;
    @FXML
    private TableColumn<TradeData, Double> totalPriceColumn;

    public void initialize() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        ObservableList<TradeData> data = FXCollections.observableArrayList();
        getWalletData(data);
        balancesTable.setItems(data);
    }

    public void handleBackButton2(){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/mainView.fxml"));
            Parent mainView = loader.load();
            Scene mainScene = new Scene(mainView);
            Stage primaryStage = (Stage) backButton2.getScene().getWindow();
            primaryStage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getWalletData(ObservableList<TradeData> data) {
            HttpClient client = HttpClient.newHttpClient();
            String url = "http://localhost:8181/api/trade-data" +
                    "?apiKey=" + apiKey +
                    "&secretKey=" + secretKey;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(responseBody -> {
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String symbol = jsonObject.getString("symbol");
                                double price = jsonObject.getDouble("price");
                                double quantity = jsonObject.getDouble("quantity");
                                double totalPrice = jsonObject.getDouble("totalPrice");
                                data.add(new TradeData(symbol, price, quantity, totalPrice));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    })
                    .join();
        }
    }


