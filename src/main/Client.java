package main;

import controller.LobbyController;
import controller.MainController;
import javafx.application.Platform;
import loader.AllCards;
import loader.GameLoader;
import loader.RoundLoader;
import org.json.JSONObject;
import utils.GeneralDialog;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Client {

    private static Client instance;
    private Socket socket;

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void createClient() {
        if (socket == null) {
            Thread thread = new Thread(() -> {
                int serverPort = 666;

                try {
                    InetAddress inetAdd = InetAddress.getByName("127.0.0.1");
                    socket = new Socket(inetAdd, serverPort);

                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();

                    PrintWriter outstr = new PrintWriter(out);
                    BufferedReader instr = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

                    System.out.println("Connection online");
                    JSONObject jsonObject = new JSONObject();
                    while (true) {
                        LobbyController.getInstance().setOffline(false);
                        if (socket.isConnected() && !socket.isClosed()) {
                            //System.out.println("Communitcating: " + GameLoader.getInstance().isIstamzug() + "  " + socket.isClosed() + "  " + socket.isConnected());
                            //System.out.println("Wrinting Something on the server");
                            if (LobbyController.getInstance() != null && GameLoader.getInstance().selectedlobby != null) {
                                GameLoader.getInstance().sendData(jsonObject);
                                outstr.println(jsonObject.toString());
                                if (GameLoader.getInstance().isIstamzug() && RoundLoader.getInstance().checkRoundOver()) {
                                    System.out.println("Gezogen: " + RoundLoader.getInstance().getCardcounter() + "   Attackiert: " + RoundLoader.getInstance().getAttackcounter());
                                    updated(jsonObject);
                                    outstr.println(jsonObject.toString());
                                }
                            } else {
                                updated(jsonObject);
                                outstr.println(jsonObject.toString());
                            }
                            outstr.flush();
                            String line = instr.readLine();
                            JSONObject readmap = new JSONObject(line);
                            if (!readmap.has("status") || (readmap.has("status") && readmap.get("status").equals(404))) {
                                System.out.println("Message was not delivered successfull");
                                break;
                            } else if (readmap.has("status")) {
                                if (readmap.has("started")) {
                                    AllCards.getInstance().splitupCards();
                                }
                                if (readmap.has("client1") && readmap.has("client2")) {
                                    if (GameLoader.getInstance().player1) {
                                        GameLoader.getInstance().enemyspielerid = readmap.getString("client2");
                                    } else {
                                        GameLoader.getInstance().enemyspielerid = readmap.getString("client1");
                                    }
                                } else if (readmap.has("update") && !readmap.getString("update").equals(GameLoader.getInstance().spielerid)) {
                                    GameLoader.getInstance().checkPlayer();
                                }
                                if (readmap.has("disconnect") && readmap.get("disconnect").equals(GameLoader.getInstance().spielerid)) {
                                    GameLoader.getInstance().logout();
                                }
                            } else {
                                System.out.println("Message contains no status");
                            }
                            LobbyController.getInstance().setOffline(false);
                            Thread.sleep(200);
                        } else {
                            throw new Exception("Server went offline");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (GameLoader.getInstance().selectedlobby != null) {
                        System.err.println("Server is not online: " + e.getMessage());
                        LobbyController.getInstance().setOffline(true);
                        Platform.runLater(() -> GeneralDialog.littleInfoDialog("Server not online", "Server not online"));
                        MainController.getInstance().setOffline(true);
                        GameLoader.getInstance().logout();
                    }
                    socket = null;
                }
            });
            thread.start();
        } else {
            System.out.println("Connection is already online");
        }
    }
    private void checkifServerisOnline(JSONObject jsonObject) {
        jsonObject.put("check", true);
    }
    private void updated(JSONObject jsonObject) {
        jsonObject.put("updated", GameLoader.getInstance().spielerid);
    }
}
