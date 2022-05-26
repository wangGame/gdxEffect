package inf112.skeleton.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;



// TODO: Implement Client methods
// TODO: Client Request

public class Client{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port){
        try{
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String msg) {
        try{
            out.println(msg);
            System.out.println("Client says: " + msg);
            String resp = in.readLine();
            System.out.println("Server says: " + resp);
            return resp;}

        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
//
//    public JSONObject requestJsonFile(JSONObject myRequest){
//        try{
//            String msg = myRequest.toString();
//            out.println(msg);
//            System.out.println("Client says: " + msg);
//            String resp = in.readLine();
//            System.out.println("Server says: " + resp);
//
//            return new JSONObject(resp);
//
//        }catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public void stopConnection(){
        try{
            in.close();
            out.close();
            clientSocket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

