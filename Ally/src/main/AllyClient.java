package main;

import AllyManager.AllyManagerController;
import Utils.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class AllyClient extends Application {

    private AllyManagerController manager;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Ally");
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(500);
        URL loginPage = getClass().getResource(Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);

            this.manager = fxmlLoader.getController();
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 700, 465);
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
