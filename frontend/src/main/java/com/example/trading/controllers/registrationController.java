package com.example.trading.controllers;

import com.example.trading.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;


public class registrationController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailTextField;
    @FXML
    private ChoiceBox<String> exchagn;
    @FXML
    private TextField apiKeyTextfield;
    @FXML
    private TextField secretKeyTextfield;
    @FXML
    private TextField strategyNameTextField;
    @FXML
    private TextField entryRuleTextField;
    @FXML
    private TextField exitRuleTextField;
    @FXML
    private TextField indicatorsInput;
   @FXML
   private Button goLogin;

    public void handleRegistration() throws SQLException, IOException, InterruptedException {
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String email = emailTextField.getText();
        String exchange = exchagn.getValue();
        String apiKey = apiKeyTextfield.getText();
        String secretKey = secretKeyTextfield.getText();


        try {
            String requestBody = "{\"email\":\"" + email + "\", \"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8181/api/signup"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                String responseBody = response.body();
                System.out.println("success");
            } else {
                // Unsuccessful signup
                showError("Error occurred during signup");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error connecting to the server");
        }

        //handle exchange account
        try {
            String requestBody = "{\"exchange_name\":\"" + exchange + "\", \"apikey\":\"" + apiKey +
                    "\", \"secretkey\":\"" + secretKey + "\", \"username\":\"" + username + "\"}";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8181/api/add-account"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201) {
                String responseBody = response.body();
                System.out.println("added to exchange account db");
            } else {
                showError("Error occurred during account addition");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error connecting to the server");
        }

    }
    public void handleStrategy() throws SQLException, IOException, InterruptedException {
        String indicators = indicatorsInput.getText();
        String name = strategyNameTextField.getText();
        String entry_rule = entryRuleTextField.getText();
        String exit_rule = exitRuleTextField.getText();
        String order_type = "both";
        String requestBody = "{\"name\":\"" + name + "\", \"order_type\":\"" + order_type + "\", \"indicators\":\"" +
                indicators +"\", \"entry_rules\":\"" + entry_rule + "\", \"exir_rules\":\"" + exit_rule + "\"}";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8181/api/create-strategy"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            String responseBody = response.body();
            System.out.println("strategy added successfully");
        } else {
            showError("Error occurred");
        }
    }



public void moveToLogin(){
    try {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/loginView.fxml"));
        Parent newView = loader.load();
        Scene scene = goLogin.getScene();
        scene.setRoot(newView);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

