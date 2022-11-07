package Login;

import AllyManager.AllyManagerController;
import Utils.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Http.HttpClientUtil;

import java.io.IOException;

import javafx.scene.layout.GridPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import static javafx.application.Application.launch;

public class LoginAlly {
    @FXML private Label errorMessage;
    @FXML private Label userNameLabel;
    @FXML private TextField userNameTextField;
    @FXML private Button loginButton;
    @FXML private Button quitButton;
    @FXML private CheckBox readyCheckBox;

    private GridPane loginComponent;
    private AllyManagerController manager;
    public LoginAlly() {

        errorMessage = new Label();
        userNameLabel = new Label();
        userNameTextField = new TextField();
        loginButton = new Button();
        quitButton = new Button();
        readyCheckBox = new CheckBox();
        //mainPage = new MainPage();
    }

    @FXML
    public void quitButtonClicked(ActionEvent event) {
        HttpClientUtil.shutdown();
        System.exit(0);
    }
    @FXML
    public void loginButtonClicked(ActionEvent event) throws IOException {

//        try {
//            uboat.switchToUboatPage(event);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        final boolean[] validLogin = {false};
        String userName = userNameTextField.getText();
        if (userName.isEmpty() || userName.equals(null)) {
            errorMessage.setText("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessage.setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessage.setText("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        errorMessage.setText("");
                        manager.setUsername(userName);
                        manager.getAllyController().setNameLabel(userName);

                        manager.getAllyController().getTabPane().getTabs().get(1).setDisable(true);
                        manager.getAllyController().getTabPane().getTabs().get(0).setDisable(false);
                        manager.getAllyController().getTabPane().getSelectionModel().select(0);
                        manager.getAllyController().startAgentDetailsListRefresher();
                        manager.switchToAllyScreen();

                    });

                }

            }
        });

    }

    public void setManager(AllyManagerController allyManagerController){

        this.manager = allyManagerController;


    }

}


