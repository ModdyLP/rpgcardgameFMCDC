package main;

import controller.LobbyController;
import controller.MainController;
import javafx.application.Platform;
import loader.GameLoader;
import utils.GeneralDialog;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class Client {

    public void createClient() {
        Thread thread = new Thread(() -> {
            int serverPort = 666;

            try {
                InetAddress inetAdd = InetAddress.getByName("127.0.0.1");
                Socket socket = new Socket(inetAdd, serverPort);

                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                ObjectOutputStream mapOutputStream = new ObjectOutputStream(out);
                ObjectInputStream mapInputStream = new ObjectInputStream(in);
                System.out.println("Connection online");
                while (true) {
                    LobbyController.getInstance().setOffline(false);
                    if (socket.isConnected() && !socket.isClosed()) {
                        //System.out.println("Communitcating: " + GameLoader.getInstance().isIstamzug() + "  " + socket.isClosed() + "  " + socket.isConnected());
                        //System.out.println("Wrinting Something on the server");
                        if(LobbyController.getInstance() != null && LobbyController.getInstance().selectedlobby != null) {
                            mapOutputStream.writeObject(GameLoader.getInstance().sendData());
                        } else {
                            mapOutputStream.writeObject(checkifServerisOnline());
                        }
                        LobbyController.getInstance().setOffline(false);
                        HashMap<String, String> readmap = (HashMap<String, String>) mapInputStream.readObject();
                        //System.out.println("Response: " + Arrays.toString(readmap.values().toArray()));
                        Thread.sleep(100);
                    } else {
                        throw new Exception("Server went offline");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Server is not online: " + e.getMessage());
                LobbyController.getInstance().setOffline(true);
                Platform.runLater(() -> GeneralDialog.littleInfoDialog("Server not online", "Server not online"));
                MainController.getInstance().setOffline(true);
                GameLoader.getInstance().logout();
            }
        });
        thread.start();

    }
    public HashMap<String, String> checkifServerisOnline() {
        HashMap<String, String> map = new HashMap<>();
        map.put("check", "true");
        return map;
    }
}
