package main;

import Uboat.Uboat;
import UboatManager.UboatControllerManager;
import Utils.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class uboatClient extends Application {

    private UboatControllerManager manager;


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Uboat");

        URL loginPage = getClass().getResource(Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            this.manager = fxmlLoader.getController();
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void stop() throws Exception {
//        //HttpClientUtil.shutdown();
//        //chatAppMainController.close();
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
