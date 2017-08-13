package main;

import controller.MainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import loader.HandCardLoader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        //Init Main Window
        Parent root = FXMLLoader.load(getClass().getResource("/controller/main.fxml"));
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
