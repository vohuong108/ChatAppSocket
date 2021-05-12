package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    // All client names, so we can check for duplicates upon registration.
    public static final Set<String> usernames = new HashSet<>(2);
    // The set of all the print writers for all the clients, used for broadcast.
    private static Set<PrintWriter> writers = new HashSet<>();

    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);


                while (true) {
                    name = in.nextLine().substring(8);
                    System.out.println("server received name: " + name);
                    if (name == null) {
                        return;
                    }
                    synchronized (usernames) {
                        if (!name.isEmpty() && !usernames.contains(name)) {
                            usernames.add(name);
                            break;
                        } else {
                            out.println("INVALID");
                        }
                    }
                }
                out.println("NAMEACCEPTED " + name);
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + " has joined");
                }
                writers.add(out);

                // Accept messages from this client and broadcast them.
                while (true) {
                    String input = in.nextLine();
                    System.out.println("Server received: " + input);
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println(name + " is leaving");
                    usernames.remove(name);
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + " has left");
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running...");
        ExecutorService pool = Executors.newFixedThreadPool(2);
        ServerSocket listener = new ServerSocket(59008);

        while (true) {
            Socket clientSocket = listener.accept();
            System.out.println("server listening client port: " + clientSocket.getRemoteSocketAddress());
            pool.execute(new Handler(clientSocket));
        }

    }
}
