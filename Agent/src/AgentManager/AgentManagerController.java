package AgentManager;


import Agent.Agent;
import Login.LoginAgent;
import Utils.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;


public class AgentManagerController {

    @FXML
    private AnchorPane mainPanel;
    private String username;

    private int numOfMissionsPerRequest;
    private int numOfThreads;

    private GridPane loginComponent ;
    private LoginAgent loginController;

    private Parent agentComponent;
    private Agent agentController;

    public AgentManagerController(){
    }

    public LoginAgent getLoginController() {
        return loginController;
    }

    public void setNumOfMissionsPerRequest(int numOfMissionsPerRequest) {
        this.numOfMissionsPerRequest = numOfMissionsPerRequest;
    }

    public void setNumOfThreads(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }

    public int getNumOfMissionsPerRequest() {
        return numOfMissionsPerRequest;
    }

    public int getNumOfThreads() {
        return numOfThreads;
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setManager(this);

            loginController.startAlliesListRefresher();
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAgentPage() {
        URL loginPageUrl = getClass().getResource(Constants.AGENT_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            agentComponent = fxmlLoader.load();
            agentController = fxmlLoader.getController();
            agentController.setManager(this);
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
        loadAgentPage();

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void switchToAgentScreen() {
        setMainPanelTo(agentComponent);

        //agentController.startListRefresher();
    }
    public void switchToLoginScreen() {
        loginController.getAlliesOptions().getSelectionModel().clearSelection();
        setMainPanelTo(loginComponent);

        //agentController.startListRefresher();
    }

    public Agent getAgentController() {
        return agentController;
    }
}
