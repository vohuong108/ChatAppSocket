package client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientChat implements Initializable {
    @FXML
    TextField input;
    @FXML
    Button btn_send;
    @FXML
    TextField chatUser;
    @FXML
    TextArea messages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_send.disableProperty().bind(
                input.textProperty().isEmpty()
        );
    }
}

