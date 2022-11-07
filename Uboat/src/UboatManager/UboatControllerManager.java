package UboatManager;

import Login.LoginUboat;
import Uboat.Uboat;
import Utils.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

import static javafx.application.Application.launch;

public class UboatControllerManager {

    @FXML
    private AnchorPane mainPanel;
    private Parent root;
    private String username;

    private GridPane loginComponent ;
    private LoginUboat loginController;

    private Parent uboatComponent;
    private Uboat uboatController;

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setManager(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUboatpage() {
        URL loginPageUrl = getClass().getResource(Constants.UBOAT_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            uboatComponent = fxmlLoader.load();
            uboatController = fxmlLoader.getController();
            uboatController.setManager(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public void setScreenSize(int width, int height) {
        this.root.prefWidth(width);
        this.root.prefHeight(height);

    }
    public void setMainPanelTo(Parent pane) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPanel.getChildren().clear();
                mainPanel.getChildren().add(pane);
                AnchorPane.setBottomAnchor(pane, 1.0);
                AnchorPane.setTopAnchor(pane, 1.0);
                AnchorPane.setLeftAnchor(pane, 1.0);
                AnchorPane.setRightAnchor(pane, 1.0);
            }
        });
    }


    public Uboat getUboat() {
        return uboatController;
    }

//    @Override
//    public void stop() throws Exception {
//        HttpClientUtil.shutdown();
//        chatAppMainController.close();
//    }
    @FXML
    public void initialize() {
        // prepare components
        loadUboatpage();
        loadLoginPage();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void switchToUboatScreen() {
        setMainPanelTo(uboatComponent);
        //chatRoomComponentController.setActive();
    }
    public void switchToLoginScreen() {
        setMainPanelTo(loginComponent);
    }

}
