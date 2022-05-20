package ru.gb.net_chat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private Map <String, String> usersChatHistory = new HashMap<>();

    @FXML
    private Label userNameField;

    @FXML
    private TextArea chatArea;

    @FXML
    private ListView<String> contacts;

    @FXML
    private TextField inputField;

    @FXML
    private Button btnSend;

    public void mockAction(ActionEvent actionEvent) {
        System.out.println("mock");
    }

    public void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void sendMessage(ActionEvent actionEvent) {
        String text = inputField.getText();
        if (text == null || text.isBlank()) {
            return;
        }
        usersChatHistory.replace(contacts.getFocusModel().getFocusedItem(),
                    usersChatHistory.get(contacts.getFocusModel().getFocusedItem()) + "Вы: " + text + "\n");
        chatArea.appendText("Вы: " + text + System.lineSeparator());
        inputField.clear();
    }

    public void listRowSelected(MouseEvent mouseEvent){
        btnSend.setDisable(false);
        userNameField.setText(contacts.getFocusModel().getFocusedItem());
        chatArea.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> names = List.of("Vasya", "Masha", "Petya", "Valera", "Nastya");
        contacts.setItems(FXCollections.observableList(names));
        for (String name:names) {
            usersChatHistory.put(name, "");
        }
        btnSend.setDisable(true);
    }

    public void refreshChat(MouseEvent mouseEvent) {
        chatArea.appendText(usersChatHistory.get(contacts.getFocusModel().getFocusedItem()));
    }

    public void openAboutForm(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/AboutWindow.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage aboutStage = new Stage();
            aboutStage.setScene(scene);
            aboutStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
