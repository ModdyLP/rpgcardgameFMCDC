package main;

import controller.MainController;
import loader.GameLoader;
import loader.RoundLoader;
import utils.GeneralDialog;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public void createClient() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int serverPort = 666;

                try {
                    InetAddress inetAdd = InetAddress.getByName("127.0.0.1");
                    Socket socket = new Socket(inetAdd, serverPort);

                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();

                    DataInputStream dIn = new DataInputStream(in);
                    DataOutputStream dOut = new DataOutputStream(out);
                    while (true) {
                        if (socket.isConnected()) {
                            System.out.println("Communitcating: "+GameLoader.getInstance().isIstamzug());
                            if (GameLoader.getInstance().isIstamzug()) {
                                System.out.println("Wrinting Something on the server");
                                dOut.writeUTF("\nRequest: "+GameLoader.getInstance().getSpielerid());
                                dOut.flush();

                                String response = dIn.readUTF();
                                System.out.println("Response: " + response);
                            }
                        } else {
                            throw new Exception("Server went offline");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Server is not online: "+e.getMessage());
                    GeneralDialog.littleInfoDialog("Server not online", "Server not online");
                    MainController.getInstance().exit();
                }
            }
        });
        thread.start();

    }
}
