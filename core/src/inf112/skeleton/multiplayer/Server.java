package inf112.skeleton.multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private int port;
    private int clientsConnected;

    public Server(int port){
        this.port = port;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            while (true){
                try{
                new Host(serverSocket, this).start();
                clientsConnected++;
                } catch (SocketException s){
                    System.out.println("Server Closed");
                    break;
                }

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void serverStop(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfClients(){
        return this.clientsConnected;
    }
}
