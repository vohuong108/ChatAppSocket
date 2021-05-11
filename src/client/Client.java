package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    String serverAddress;
    Scanner in;
    PrintWriter out;



    public Client(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    private void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            System.out.println("client: " + socket.getLocalSocketAddress());
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine();

            }
        } finally {

        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost");
        client.run();
    }
}
