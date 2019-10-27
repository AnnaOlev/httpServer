package com.company;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static int port = 8080;
    private ServerSocket serverSocket;
    private List<Dog> dogs = new ArrayList<>();

    public static void main(String[] args) {
        try {
            System.out.println("==Server info==");
            System.out.println("Server machine " + InetAddress.getLocalHost().getCanonicalHostName());
            System.out.println("Port number " + port);
            System.out.println();
        } catch (UnknownHostException e1){
            e1.printStackTrace();
        }

        try {
            Server server = new Server();
            server.start();
        } catch (IOException e) {
            System.err.println("Error " + e.getMessage());
            System.exit(0);
        }
    }

    private Server() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    private void start(){
        try {
            while (true) {

                Socket socket = serverSocket.accept();
                HttpWorker connection = new HttpWorker(socket, dogs);

                Thread thread = new Thread(connection);
                thread.start();
                //подумать как здесь норм сделать
            }
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
