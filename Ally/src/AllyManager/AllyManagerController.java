package AllyManager;

import Ally.Ally;
import Login.LoginAlly;
import Utils.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;


public class AllyManagerController {

    @FXML
    private AnchorPane mainPanel;


    private String username;

    private GridPane loginComponent ;
    private LoginAlly loginController;

    private Parent allyComponent;
    private Ally allyController;

    public AllyManagerController(){

    }

    public LoginAlly getLoginController() {
        return loginController;
    }

    public Ally getAllyController() {
        return allyController;
    }

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

    private void loadAllypage() {
        URL loginPageUrl = getClass().getResource(Constants.ALLY_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            allyComponent = fxmlLoader.load();
            allyController = fxmlLoader.getController();
            allyController.setManager(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setMainPanelTo(Parent pane) {
        Platform.runLater(() -> {
            mainPanel.getChildren().clear();
            mainPanel.getChildren().add(pane);
            AnchorPane.setBottomAnchor(pane, 1.0);
            AnchorPane.setTopAnchor(pane, 1.0);
            AnchorPane.setLeftAnchor(pane, 1.0);
            AnchorPane.setRightAnchor(pane, 1.0);
        });
    }


    //    @Override
//    public void stop() throws Exception {
//        HttpClientUtil.shutdown();
//        chatAppMainController.close();
//    }
    @FXML
    public void initialize() {
        // prepare components
        loadLoginPage();
        loadAllypage();

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void switchToAllyScreen() {
        setMainPanelTo(allyComponent);
        allyController.startContestListRefresher();
        //chatRoomComponentController.setActive();
    }
    public void switchToLoginScreen() {
        setMainPanelTo(loginComponent);
    }
}
