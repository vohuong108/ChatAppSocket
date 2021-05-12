package client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientVerify implements Initializable {
    @FXML
    TextField username;
    @FXML
    Button submit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submit.disableProperty().bind(
                username.textProperty().isEmpty()
        );
    }

}
