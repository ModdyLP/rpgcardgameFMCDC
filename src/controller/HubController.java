package controller;

import javafx.application.Platform;

public class HubController {


    
    public void setStatus(String status) {
        Platform.runLater(()-> this.status.setText(status));
    }
}
