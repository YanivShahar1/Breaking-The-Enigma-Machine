package main;

import AgentManager.AgentManagerController;
import Utils.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class AgentClient extends Application {

    private AgentManagerController manager;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Agent");
        primaryStage.setMinHeight(350);
        primaryStage.setMinWidth(500);
        URL loginPage = getClass().getResource(Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);

            this.manager = fxmlLoader.getController();
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 650, 396);
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
