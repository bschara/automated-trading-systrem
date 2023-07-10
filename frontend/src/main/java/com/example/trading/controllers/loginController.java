package com.example.trading.controllers;

import com.example.trading.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.prefs.Preferences;

public class loginController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;
    @FXML
    public Hyperlink signupLink;
public  loginController() {}
    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8181/api/login?username=" + username + "&password=" + password))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/mainView.fxml"));
                Parent mainView = loader.load();
                Scene mainScene = new Scene(mainView);
                Stage primaryStage = (Stage) loginButton.getScene().getWindow();
                primaryStage.setScene(mainScene);
            } else {
                // Unsuccessful login
                showError("Invalid username or password");
            }
        } catch (IOException | InterruptedException e) {
            // Handle exceptions
            e.printStackTrace();
            showError("Error connecting to the server");
        }
        Preferences prefs = Preferences.userNodeForPackage(loginController.class);
        prefs.put("username", username);
     getCredentials(username);
}
    @FXML
    private void handleSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/registrationView.fxml"));
            Parent newView = loader.load();
            Scene scene = signupLink.getScene();
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

    public void getCredentials(String username) {
        String API_URL = "http://localhost:8181/api/get-credentials";

        String requestBody = "?username=" + username;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + requestBody))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    try {
                        JSONObject responseJson = new JSONObject(responseBody);
                        String apiKey = responseJson.getString("apikey");
                        String secretKey = responseJson.getString("secretkey");
                        Preferences prefs = Preferences.userNodeForPackage(loginController.class);
                        prefs.put("apiKey", apiKey);
                        prefs.put("secretKey", secretKey);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }
}


