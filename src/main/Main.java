package main;

import controller.MainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import loader.HandCardLoader;
import storage.FileDriver;
import storage.MongoDBConnector;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FileDriver.getInstance().createNewFile();
        MongoDBConnector.getInstance().connect();
        //Init Main Window
        Parent root = FXMLLoader.load(MainController.class.getResource("/main.fxml"));
        primaryStage.setTitle("Proelignis");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                MainController.getInstance().exit();
            }
        });
        MainController.getInstance().setPrimarystage(primaryStage);
        HandCardLoader.getInstance().loadCards();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
