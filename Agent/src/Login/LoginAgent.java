package Login;

import AgentManager.AgentManagerController;
import Utils.Constants;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import Http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import static Utils.Constants.REFRESH_RATE;
import static javafx.application.Application.launch;

public class LoginAgent implements Initializable {
    @FXML private Label errorMessage;

    @FXML private GridPane loginGridPane;
    @FXML private Label userNameLabel;
    @FXML private TextField userNameTextField;
    @FXML private TextField numOfMissionsPerRequest;
    @FXML private Slider threadsSlider;

    @FXML
    private ComboBox<String> alliesOptions;

    @FXML private Button loginButton;

    private boolean isUserNameSet;
    private boolean isNumOfMissionsPerRequestSet;
    private boolean isAllySet;

    private String userName;
    private int numOfMissionsToPull;
    private String allyName;


    private Timer timer;
    private TimerTask listRefresher;
    private final BooleanProperty autoUpdate;
    private AgentManagerController manager;
    public LoginAgent() {
        autoUpdate = new SimpleBooleanProperty(true);

        errorMessage = new Label();
        userNameLabel = new Label();
        userNameTextField = new TextField();
        threadsSlider = new Slider();
        numOfMissionsPerRequest = new TextField();

        loginGridPane = new GridPane();
        alliesOptions = new ComboBox<>();

    }

    public ComboBox<String> getAlliesOptions() {
        return alliesOptions;
    }

    public void clearAlliesOptions() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                alliesOptions.getItems().clear();
            }
        });
    }
    @FXML
    public void quitButtonClicked(ActionEvent event) {
        HttpClientUtil.shutdown();
        System.exit(0);
    }
    @FXML
    public void loginButtonClicked(ActionEvent event) throws IOException {

        final boolean[] validLogin = {false};
        String userName = userNameTextField.getText();
        if (userName.isEmpty() || userName.equals(null)) {
            errorMessage.setText("User name is empty. You can't login with empty user name");
            return;
        }

        manager.setNumOfMissionsPerRequest(numOfMissionsToPull);
        manager.setNumOfThreads((int)threadsSlider.getValue());

        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("allyName", allyName)
                .addQueryParameter("numOfMissionsPerRequest", String.valueOf(numOfMissionsToPull))
                .addQueryParameter("numOfThreads",String.valueOf(manager.getNumOfThreads()) )
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
                    //System.out.println("code = " + response.code());
                    //System.out.println(response.body().string());
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessage.setText("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        errorMessage.setText("");
                        loginButton.setDisable(true);
                        manager.getAgentController().setAllyName(alliesOptions.getSelectionModel().getSelectedItem());
                        manager.getAgentController().setNameLabel(userName);
                        manager.switchToAgentScreen();
                        manager.setUsername(userName);

                        stopListRefresher();
                        manager.getAgentController().askServerForDTOsContestData();
                        manager.getAgentController().askServerIfEveryoneIsReady();
                    });

                }

            }
        });

    }

    public void setManager(AgentManagerController agentManagerController){
        this.manager = agentManagerController;

    }

    public void startAlliesListRefresher() {

        listRefresher = new AlliesListRefresher(
                autoUpdate,
                this::updateAlliesList);
        timer = new Timer();

        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void stopListRefresher() {
        if (timer != null && listRefresher != null) {
            timer.cancel();
            listRefresher.cancel();
        }
    }

    private void updateAlliesList(Set<String> allAllies) {
        Platform.runLater(() -> {
            ObservableList<String > items = alliesOptions.getItems();
            String ally = alliesOptions.getValue();
            items.clear();
            items.addAll(allAllies);
            alliesOptions.setValue(ally);

//            allAllies.forEach(allyName ->alliesOptions.getItems().add(allyName));
        });
    }

    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        }catch(NumberFormatException e) {
            return false;
        }
    }

    public void invokeErrorAlert(String errorMessage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText(errorMessage);
                a.setHeaderText("ERROR");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.show();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numOfMissionsPerRequest.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty() && isInteger(newValue) && Integer.parseInt(newValue) > 0) {
                    isNumOfMissionsPerRequestSet = true;
                    numOfMissionsToPull = Integer.parseInt(newValue);
                    if (isAllySet && isUserNameSet) {
                        loginButton.setDisable(false);
                    } else {
                        loginButton.setDisable(true);
                    }

                } else {
                    numOfMissionsToPull = 0;
                    isNumOfMissionsPerRequestSet = false;
                    loginButton.setDisable(true);
                    numOfMissionsPerRequest.setText("");
                    invokeErrorAlert("Invalid number of missions\nNumber of missions per request should be integer and greater than 0 ");


                }
            }
        });

        userNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                isUserNameSet = true;
                userName = newValue;
                if (isAllySet && isNumOfMissionsPerRequestSet) {
                    loginButton.setDisable(false);
                } else {
                    loginButton.setDisable(true);
                }
            }
            else {
                userName = "";
                isUserNameSet = false;
                loginButton.setDisable(true);
                invokeErrorAlert("User name is empty!\n");
            }
        });

        alliesOptions.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (alliesOptions.getItems().isEmpty()) {
                    return;
                }
                if (newValue.intValue() < 0) {
                    return;
                }
                if (newValue != null && !newValue.equals("")) {
                    isAllySet = true;
//                    System.out.println(newValue.intValue());
//                    System.out.println(oldValue.intValue());
//                    System.out.println(alliesOptions.getItems());
                    allyName = alliesOptions.getItems().get(newValue.intValue());
                    if (isUserNameSet && isNumOfMissionsPerRequestSet) {
                        loginButton.setDisable(false);
                    } else {
                        loginButton.setDisable(true);
                    }
                } else {
                    allyName = "";
                    isAllySet = false;
                    loginButton.setDisable(true);
                }
            }
        });
    }
}


