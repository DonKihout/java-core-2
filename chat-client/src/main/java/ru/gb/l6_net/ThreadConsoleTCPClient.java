package ru.gb.l6_net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadConsoleTCPClient {
    private static final String HOST = "192.168.0.104";
    private static final int PORT = 8189;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread clientThread;

    public static void main(String[] args) {
        new ThreadConsoleTCPClient().start();
    }

    private void start() {
        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            startConsoleInput();

            while (!socket.isClosed()) {
                String income = in.readUTF();
                System.out.println(income);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void shutdown() throws IOException {
        if (clientThread != null && clientThread.isAlive()) {
            clientThread.interrupt();
        }
        System.out.println("Client stopped");
    }

    private void startConsoleInput() {
        clientThread = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.print("Enter your message >>>>> ");
                while (!Thread.currentThread().isInterrupted()) {
                    if (br.ready()) {
                        String outcome = HOST + ": " + br.readLine();
                        out.writeUTF(outcome);

                        if (br.readLine().equals("/end")) {
                            shutdown();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }
}
