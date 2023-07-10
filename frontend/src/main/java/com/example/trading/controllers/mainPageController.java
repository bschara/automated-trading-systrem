package com.example.trading.controllers;

import com.example.trading.Main;
import com.example.trading.util.NewsItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class mainPageController {

    public Button tab1Button;
    public Button tab2Button;
    public Button tab3Button;
    public Button tab4Button;

    @FXML
    private TableView newsTable;

    @FXML
    private TableColumn<NewsItem, String> titleColumn1;

    @FXML
    private TableColumn<NewsItem, String> descriptionColumn1;

    @FXML
    private TableColumn<NewsItem, String> linkColumn1;

    @FXML
    private TableColumn<NewsItem, String> dateColumn1;


    public void initialize() throws IOException {
//        displayNews();
    }
    @FXML
    private void handleOption1Action(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/dashboardView.fxml"));
            Parent newView = loader.load();
            Scene scene = tab1Button.getScene();
            scene.setRoot(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOption2Action(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/tradingView.fxml"));
        Parent newView = loader.load();
        Scene scene = tab2Button.getScene();
        scene.setRoot(newView);
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void handleOption3Action(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/portfolioView.fxml"));
            Parent newView = loader.load();
            Scene scene = tab3Button.getScene();
            scene.setRoot(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleOption4Action(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/automatedStrategyView.fxml"));
            Parent newView = loader.load();
            Scene scene = tab4Button.getScene();
            scene.setRoot(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void displayNews() {
        // Create the columns
        TableColumn<NewsItem, String> titleColumn = titleColumn1;
        TableColumn<NewsItem, String> descriptionColumn = descriptionColumn1;
        TableColumn<NewsItem, String> linkColumn = linkColumn1;
        TableColumn<NewsItem, String> dateColumn = dateColumn1;

        // Populate the table with data from the API
        ObservableList<NewsItem> data = FXCollections.observableArrayList();
        try {
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
            JSONArray newsArray = new JSONArray(jsonResponse);
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject newsItem = newsArray.getJSONObject(i);
                String title = newsItem.getString("title");
                String description = newsItem.getString("description");
                String link = newsItem.getString("url");
                String date = newsItem.getString("date");
                data.add(new NewsItem(title, description, link, date));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        // Set the data on the news table
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        linkColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLink()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        newsTable.setItems(data);
    }
}


