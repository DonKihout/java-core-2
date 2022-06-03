package ru.gb.l6_net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ThreadConsoleTCPServer {
    private static final int PORT = 8189;
    private Socket socket;
    private List<Handler> handlers = new ArrayList<>();

    public static void main(String[] args) {
        new ThreadConsoleTCPServer().start();
    }

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started");
            waitConnection(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitConnection(ServerSocket serverSocket) throws IOException {
        while (true){
            System.out.println("Waiting for connection....");
            socket = serverSocket.accept();
            System.out.println("Client " + socket.getLocalAddress().getHostAddress() + " connected");
            Handler handler = new Handler(socket, this);
            handlers.add(handler);
            handler.handle();
        }
    }

    public void mailing(String message){
        for (Handler handler: handlers) {
            handler.send(message);
        }
    }
}
