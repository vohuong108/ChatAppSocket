package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Application implements EventHandler<ActionEvent> {
    String serverAddress;
    Scanner in;
    public static PrintWriter out;
    public static ClientChat clientChat;
    public static ClientVerify clientVerify;
    public static Stage primaryStage;


    @Override
    public void start(Stage Stage) throws Exception {
        primaryStage = Stage;
        primaryStage.setTitle("Client Chat");
        primaryStage.setScene(setSceneClientVerify());
        primaryStage.show();
        connectToServer();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == clientVerify.submit) {
            String username = clientVerify.username.getText();
            verifyUsername(username);

            String res = in.nextLine();
            if (res.startsWith("NAMEACCEPTED")) {
                try {
                    primaryStage.setScene(setSceneClientChat());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                primaryStage.show();
                clientChat.chatUser.setText(res.substring(12));

                Runnable receiver = () -> {
                    while (in.hasNextLine()) {
                        String line = in.nextLine();
                        System.out.println("received from server: " + line);

                        if (line.startsWith("MESSAGE")) {
                            clientChat.messages.appendText(line.substring(8) + "\n");
                        }
                    }
                };
                Thread receiver_thread = new Thread(receiver);

                receiver_thread.start();
            }
            else {
                clientVerify.username.clear();
                clientVerify.username.setText("Username đã tồn tại");

            }

        }
        else if (event.getSource() == clientChat.btn_send) {
            clientChat.input.requestFocus();
            String msg = clientChat.input.getText();
            out.println(msg);
            clientChat.input.setText("");
        }
    }

    public Scene setSceneClientVerify() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../client/clientVerify.fxml"));
        Parent parent = loader.load();
        clientVerify = loader.getController();
        clientVerify.submit.setOnAction(this::handle);
        return new Scene(parent, 600, 260);
    }

    public Scene setSceneClientChat() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../client/clientChat.fxml"));
        Parent parent = loader.load();
        clientChat = loader.getController();
        clientChat.btn_send.setOnAction(this::handle);
        return new Scene(parent, 600, 600);
    }

    public void connectToServer() throws IOException {
        Socket socket = new Socket(serverAddress, 59008);
        System.out.println("Local Client Socket: " + socket.getLocalSocketAddress());
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void verifyUsername(String username) {
        out.println("USERNAME" + username);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
