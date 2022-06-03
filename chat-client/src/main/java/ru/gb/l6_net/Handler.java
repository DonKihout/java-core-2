package ru.gb.l6_net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Handler {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Thread handlerThread;
    private ThreadConsoleTCPServer server;

    public Handler(Socket socket, ThreadConsoleTCPServer server){
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handle() {
        handlerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                try {
                    String message = in.readUTF();
                    server.mailing(message);
                    System.out.println("Client " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        handlerThread.start();
    }

    public void send(String message){
        try{
            out.writeUTF(message);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
