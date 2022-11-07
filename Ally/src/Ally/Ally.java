package Ally;

import AllyManager.AllyManagerController;
import DTO.*;
import Http.HttpClientUtil;
import SystemExceptions.ConnectionWithServerFailed;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.util.*;

import static Utils.Constants.CHAT_LINE_FORMATTING;
import static Utils.Constants.REFRESH_RATE;

public class Ally implements Initializable {

    @FXML private Button confirmButton;
    @FXML private Label messageForUserLabelTabContest;
    @FXML private Label messageForUserLabelTabDashboard;
    @FXML  private Label nameLabel;
    private ContestListRefresher contestListRefresher;
    @FXML private TextField chosenBattleField;
    @FXML private TableView<DTOAgentDetails> agentDetailsTableView;
    @FXML private TableColumn<DTOAgentDetails, String> agentNameColumnDTOAgentDetails;
    @FXML private TableColumn<DTOAgentDetails, Integer> numOfThreadsColumn;
    @FXML private TableColumn<DTOAgentDetails, Integer>numOfMissionsColumn;


    @FXML private TableView<DTOContestData> allContestsTableViewDTOContestDataTabDashboard;
    @FXML private TableColumn<DTOContestData, String> uboatNameDTOContestDataTabDashboard;
    @FXML private TableColumn<DTOContestData, String> battleFieldNameDTOContestDataTabDashboard;
    @FXML private TableColumn<DTOContestData, String> statusDTOContestDataTabDashboard;
    @FXML private TableColumn<DTOContestData, Integer> registeredAlliesDTOContestDataTabDashboard;
    @FXML private TableColumn<DTOContestData, Integer> alliesNeededDTOContestDataTabDashboard;
    @FXML private TableColumn<DTOContestData, String> levelDTOContestDataTabDashboard;


//    @FXML private TableView<DTOContestData> contestTableViewDTOContestDataTabContest;
//    @FXML private TableColumn<DTOContestData, String> uboatNameDTOContestDataTabContest;
//    @FXML private TableColumn<DTOContestData, String> battleFieldNameDTOContestDataTabContest;
//    @FXML private TableColumn<DTOContestData, String> statusDTOContestDataTabContest;
//    @FXML private TableColumn<DTOContestData, Integer> registeredAlliesDTOContestDataTabContest;
//    @FXML private TableColumn<DTOContestData, Integer> alliesNeededDTOContestDataTabContest;
//    @FXML private TableColumn<DTOContestData, String> levelDTOContestDataTabContest;


    @FXML private TableView<DTODecryptionCandidate> teamCandidatesDTODecryptionCandidateTableView;
    @FXML private TableColumn<DTODecryptionCandidate, String> decryptionDTODecryptionCandidate;
    @FXML private TableColumn<DTODecryptionCandidate, String> agentNameDTODecryptionCandidate;
    @FXML private TableColumn<DTODecryptionCandidate, String> configurationDTODecryptionCandidate;


    @FXML private TableView<DTOAllyTeamDetails> allyTeamDetailsTableView;
    @FXML private TableColumn<DTOAllyTeamDetails, String> allyNameColumnDTOAllyTeamDetails;
    @FXML private TableColumn<DTOAllyTeamDetails, Integer> numOfAgentsColumnDTOAllyTeamDetails;
    @FXML private TableColumn<DTOAllyTeamDetails, Integer> missionSizeColumnDTOAllyTeamDetails;


    @FXML private TableView<DTOAgentsProgressData> agentsProgressDataTableView;
    @FXML private TableColumn<DTOAgentsProgressData, String> agentNameColumnDTOAgentsProgressData;
    @FXML private TableColumn<DTOAgentsProgressData, Long> numOfDoneMissionsDTOAgentsProgressData;
    @FXML private TableColumn<DTOAgentsProgressData, Long> totalMissionsDTOAgentsProgressData;
    @FXML private TableColumn<DTOAgentsProgressData, Integer> numOfCandidatesDTOAgentsProgressData;

    @FXML private Button readyButton;
    @FXML private TabPane tabPane;
    @FXML private TextField missionSizeTextField;
    @FXML private TextArea contestDataTextAreaTabContest;
    @FXML private Button logoutButton;

    private String chosenUboat;
    private final BooleanProperty autoUpdate;
    private Timer contestTimer;
    private Timer agentTimer;
    private TimerTask allContestsListRefresher;
    private TimerTask agentDetailslistRefresher;
    private Timer contestTabDTOsTimer;
    private TimerTask contestTabDTOsRefresher;
    private boolean isMissionSizeSet = false;
    private boolean isUboatSelected = false;
    private boolean[] isWinnerWindowInvoked;
    private AllyManagerController manager;
    private List<DTOAgentDetails> agentsRegisteredToAlly;
    private boolean atLeastOneAgentInAlly;
    public Ally() {
        autoUpdate = new SimpleBooleanProperty(true);
        allContestsTableViewDTOContestDataTabDashboard = new TableView<>();
        battleFieldNameDTOContestDataTabDashboard = new TableColumn<>();
        uboatNameDTOContestDataTabDashboard = new TableColumn<>();
        statusDTOContestDataTabDashboard = new TableColumn<>();
        levelDTOContestDataTabDashboard = new TableColumn<>();
        registeredAlliesDTOContestDataTabDashboard = new TableColumn<>();
        alliesNeededDTOContestDataTabDashboard = new TableColumn<>();
        tabPane = new TabPane();
        chosenBattleField = new TextField();

        agentDetailsTableView = new TableView<>();
        agentNameColumnDTOAgentDetails = new TableColumn<>();
        numOfMissionsColumn = new TableColumn<>();
        numOfThreadsColumn = new TableColumn<>();

        isWinnerWindowInvoked = new boolean[1];

        nameLabel = new Label();
        messageForUserLabelTabContest = new Label();
        confirmButton = new Button();
        contestDataTextAreaTabContest = new TextArea();
        logoutButton = new Button();

        agentsRegisteredToAlly = new ArrayList<>();
        atLeastOneAgentInAlly = false;
    }

    public void setNameLabel(String nameLabel) {
        this.nameLabel.setText(nameLabel);
    }

    public TabPane getTabPane() {
        return tabPane;
    }


    @FXML
    public void joinButtonClicked(ActionEvent e) {
        DTOContestData dtoContestData = allContestsTableViewDTOContestDataTabDashboard.getSelectionModel().getSelectedItem();
        if (dtoContestData == null) {
            isUboatSelected = false;
            return;
        }
        //chane this !! the server should say that
        if(dtoContestData.getNumOfAlliesNeeded() == dtoContestData.getNumOfRegisteredAllies()){
            invokeErrorAlert("The contest of " + dtoContestData.getBattleFieldName() + " is full!!");
            isUboatSelected = false;
            return;
        }
        if (dtoContestData.getStatusAsContestStatus() != ContestStatus.WAITING) {
            invokeErrorAlert("The contest of " + dtoContestData.getBattleFieldName() + " is not available!!");
            isUboatSelected = false;
            return;
        }
        if (isMissionSizeSet && atLeastOneAgentInAlly) {
            readyButton.setDisable(false);
        }
        chosenBattleField.setText(dtoContestData.getBattleFieldName());
        chosenUboat = dtoContestData.getUboatName();
        isUboatSelected = true;
    }
    @FXML
    public void readyButtonClicked(ActionEvent e) throws ConnectionWithServerFailed {

        isWinnerWindowInvoked[0] = false;

        final boolean[] errorOccurred = {false};
        final String[] errorDetails = {""};

        String finalUrl = HttpUrl
                .parse(Constants.READY_FOR_CONTEST)
                .newBuilder()
                .addQueryParameter("uboatName", chosenUboat)
                .addQueryParameter("allyName", manager.getUsername())
                .addQueryParameter("missionSize", missionSizeTextField.getText())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorOccurred[0] = true;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    errorDetails[0] = responseBody;
                    errorOccurred[0] = true;
                }
                else {
                    Platform.runLater(() -> {
                        messageForUserLabelTabDashboard.setText("");

                        switchToTabPaneContest();
                        stopAgentDetailsListRefresher();
                        stopContestListRefresher();
                        askServerForDTOsContestData();
                    });

                }
            }
        });
        if (errorOccurred[0]) {
            throw new ConnectionWithServerFailed(errorDetails[0]);
        }
    }
    public void switchToTabPaneDashboard() {
        tabPane.getSelectionModel().select(0);
//        tabPane.getTabs().get(1).setDisable(true);
        tabPane.getTabs().get(0).setDisable(false);

        agentsProgressDataTableView.getItems().clear();
        contestDataTextAreaTabContest.clear();
        teamCandidatesDTODecryptionCandidateTableView.getItems().clear();
        allyTeamDetailsTableView.getItems().clear();
        allContestsTableViewDTOContestDataTabDashboard.getSelectionModel().clearSelection();
    }
    public void switchToTabPaneContest() {
        tabPane.getSelectionModel().select(1);
        tabPane.getTabs().get(0).setDisable(true);
        tabPane.getTabs().get(1).setDisable(false);
        missionSizeTextField.clear();
        chosenBattleField.clear();
        allContestsTableViewDTOContestDataTabDashboard.getSelectionModel().clearSelection();
        isUboatSelected = false;
        isMissionSizeSet = false;
    }
    public void askServerForDTOsContestData() {
        contestTabDTOsRefresher = new DTOsContestDataRefresher(
                isWinnerWindowInvoked,
                manager,
                autoUpdate,
                this::updateContestListTabContest,
                this::updateContestTeamsListRefresher,
                this::updateTeamsAgentsProgressData,
                this::updateTeamDecryptionCandidates);
        contestTabDTOsTimer = new Timer();
        contestTabDTOsTimer.schedule(contestTabDTOsRefresher, 0, REFRESH_RATE);
    }
    public void stopAskingServerForDTOsContestData() {
        if (contestTabDTOsTimer != null && contestTabDTOsRefresher != null) {
            contestTabDTOsTimer.cancel();
            contestTabDTOsRefresher.cancel();
        }
    }
    public void getTheContestAllyNameWinner() {

        String finalUrl = HttpUrl
                .parse(Constants.GET_WINNER)
                .newBuilder()
                .addQueryParameter("userType", "ally")
                .addQueryParameter("userName", manager.getUsername())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                }
                else {
                    String winnerAlly = response.body().string();

                    if (!winnerAlly.equals("") && !winnerAlly.equals("null")) {
                        invokeWinnerWindow(winnerAlly);
                    }
                    else {
                        Platform.runLater(() -> {
                            messageForUserLabelTabContest.setText("uboat logged out");
                            messageForUserLabelTabContest.setTextFill(Color.RED);
                            confirmButton.setDisable(false);
                            removeAllyFromContest();
                        });
                    }
                    missionSizeTextField.clear();
                    chosenBattleField.clear();
                }
               // removeAllyFromContest();
            }
        });
    }
    @FXML
    public void logoutButtonClicked() {

        String finalUrl = HttpUrl
                .parse(Constants.LOGOUT_ALLY)
                .newBuilder()
                .addQueryParameter("allyName", manager.getUsername())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();
                }
                else {
                    stopAgentDetailsListRefresher();
                    stopAskingServerForDTOsContestData();
                    //removeAllyFromContest();
                    tabPane.getTabs().get(1).setDisable(true);
                    tabPane.getTabs().get(0).setDisable(false);
                    tabPane.getSelectionModel().select(0);
                    manager.switchToLoginScreen();
                    confirmButtonClicked();
                    agentsRegisteredToAlly.clear();
                    response.close();
                }
            }
        });
    }
    public void resetAllAllyPage() {
        Platform.runLater(() -> {
            allyTeamDetailsTableView.getItems().clear();
            teamCandidatesDTODecryptionCandidateTableView.getItems().clear();
            agentsProgressDataTableView.getItems().clear();
            allContestsTableViewDTOContestDataTabDashboard.getItems().clear();
            agentDetailsTableView.getItems().clear();
            contestDataTextAreaTabContest.clear();
            messageForUserLabelTabContest.setText("");
            missionSizeTextField.clear();
            chosenBattleField.clear();
            confirmButton.setDisable(true);
        });
    }
    public void resetAllAllyData() {
        isMissionSizeSet = false;
        isUboatSelected = false;
        isWinnerWindowInvoked[0] = false;
        atLeastOneAgentInAlly = false;
        chosenUboat = "";
    }
    public void removeAllyFromContest() {

        String finalUrl = HttpUrl
                .parse(Constants.REMOVE_ALLY_FROM_CONTEST)
                .newBuilder()
                .addQueryParameter("allyName", manager.getUsername())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {

                }
                response.close();
            }
        });
    }
    public void invokeWinnerWindow(String allyName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageForUserLabelTabContest.setText("Winner: " + allyName + " Team!");
                messageForUserLabelTabContest.setTextFill(Color.GREEN);
                confirmButton.setDisable(false);
            }
        });
    }
    @FXML public void confirmButtonClicked() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                resetAllAllyData();
                resetAllAllyPage();
                confirmButton.setDisable(true);
                tellServerAllyApprovedGameOver();
                switchToTabPaneDashboard();
                messageForUserLabelTabContest.setText("");
                messageForUserLabelTabDashboard.setText("");
                isWinnerWindowInvoked[0] = true;
                tabPane.getTabs().get(1).setDisable(true);
                tabPane.getTabs().get(0).setDisable(false);
                tabPane.getSelectionModel().select(0);
                removeAllyFromContest();
                startAgentDetailsListRefresher();
                startContestListRefresher();
            }
        });
    }
    public void tellServerAllyApprovedGameOver() {
        String finalUrl = HttpUrl
                .parse(Constants.GAME_OVER_APPROVED)
                .newBuilder()
                .addQueryParameter("allyName", manager.getUsername())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {

                }
                response.close();
            }
        });
    }
    public void updateTeamDecryptionCandidates(List<DTODecryptionCandidate> decryptionCandidates) {
        Platform.runLater(() -> {
            ObservableList<DTODecryptionCandidate> items = teamCandidatesDTODecryptionCandidateTableView.getItems();
            items.clear();
            decryptionCandidates.forEach(dtoDecryptionCandidate ->teamCandidatesDTODecryptionCandidateTableView.getItems().add(dtoDecryptionCandidate));
        });
    }
    public void updateTeamsAgentsProgressData(List<DTOAgentsProgressData> agentsProgressData) {
        Platform.runLater(() -> {
            ObservableList<DTOAgentsProgressData> items = agentsProgressDataTableView.getItems();
            items.clear();
            agentsProgressData.forEach(dtoAgentsProgressData ->agentsProgressDataTableView.getItems().add(dtoAgentsProgressData));
        });
    }
    public void updateContestTeamsListRefresher(List<DTOAllyTeamDetails> allyTeamDetails) {
        Platform.runLater(() -> {
            ObservableList<DTOAllyTeamDetails> items = allyTeamDetailsTableView.getItems();
            items.clear();
            allyTeamDetails.forEach(dtoAllyTeamDetails ->allyTeamDetailsTableView.getItems().add(dtoAllyTeamDetails));
        });
    }
    public void updateContestListTabContest(DTOContestData allyTeamDetails) {
        Platform.runLater(() -> {
            contestDataTextAreaTabContest.clear();
            if (allyTeamDetails == null) {
                return;
            }
            contestDataTextAreaTabContest.setText("Uboat: " + allyTeamDetails.getUboatName());
            contestDataTextAreaTabContest.appendText("  BattleField: " + allyTeamDetails.getBattleFieldName());
            contestDataTextAreaTabContest.appendText("  Level: " + allyTeamDetails.getLevel());
            contestDataTextAreaTabContest.appendText("  Status: " + allyTeamDetails.getStatus());
            contestDataTextAreaTabContest.appendText("  Allies needed / registered: " + allyTeamDetails.getNumOfAlliesNeeded() + " / " +  allyTeamDetails.getNumOfRegisteredAllies());

        });
    }
    public void startContestListRefresher() {
        allContestsListRefresher = new ContestListRefresher(
                autoUpdate,
                this::updateContestsListTabDashboard);
        contestTimer = new Timer();

        contestTimer.schedule(allContestsListRefresher, 0, REFRESH_RATE);
    }
    public void stopContestListRefresher() {
        if (contestTimer != null && allContestsListRefresher != null) {
            contestTimer.cancel();
            allContestsListRefresher.cancel();
        }
    }
    public void startAgentDetailsListRefresher() {
        //System.out.println("in startAgentDetailsListRefresher ");
        agentDetailslistRefresher = new TeamDetailsRefresher(
                manager.getUsername(),
                autoUpdate,
                this::updateAgentsDetails);
        agentTimer = new Timer();

        agentTimer.schedule(agentDetailslistRefresher, 0, REFRESH_RATE);
    }
    public void stopAgentDetailsListRefresher() {
        if (agentTimer != null && agentDetailslistRefresher != null) {
            agentTimer.cancel();
            agentDetailslistRefresher.cancel();
        }
    }

    private void updateContestsListTabDashboard(List<DTOContestData> allContests) {
        Platform.runLater(() -> {
            ObservableList<DTOContestData> items = allContestsTableViewDTOContestDataTabDashboard.getItems();
            int index = allContestsTableViewDTOContestDataTabDashboard.getSelectionModel().selectedIndexProperty().get();
            items.clear();
            allContests.forEach(dtoContestData -> allContestsTableViewDTOContestDataTabDashboard.getItems().add(dtoContestData));
            allContestsTableViewDTOContestDataTabDashboard.getSelectionModel().select(index);

        });
    }
    private void updateAgentsDetails(List<DTOAgentDetails> allAgents) {
        //System.out.println("in updateAgentsDetails allAgents =  " + allAgents);
        if(allAgents==null){
            readyButton.setDisable(true);
            Platform.runLater(() -> {agentDetailsTableView.getItems().clear();
                if (atLeastOneAgentInAlly) {
                    messageForUserLabelTabDashboard.setText("All agents left the ally / No Agents have joined yet");
                    messageForUserLabelTabDashboard.setTextFill(Color.RED);
                    atLeastOneAgentInAlly = false;
                    agentsRegisteredToAlly.clear();
                }});
            return;
        }
        else {
            atLeastOneAgentInAlly = true;
        }
        if (allAgents.isEmpty()) {
            readyButton.setDisable(true);
            atLeastOneAgentInAlly = false;
        }
        else {
            atLeastOneAgentInAlly = true;
            //Platform.runLater(() -> messageForUserLabelTabDashboard.setText(""));
            if (isMissionSizeSet && isUboatSelected) {
                readyButton.setDisable(false);
            }
        }


        if (allAgents.size() > agentsRegisteredToAlly.size()) {
            Platform.runLater(() -> messageForUserLabelTabDashboard.setText(""));
        }

        Platform.runLater(() -> {
            agentDetailsTableView.getItems().clear();
            if (agentsRegisteredToAlly.size() > allAgents.size()) {
                List<String> leftAgents = new ArrayList<>();
                List<String> registeredAgentName = new ArrayList<>();
                List<String> allAgentName = new ArrayList<>();
                agentsRegisteredToAlly.forEach(registered -> registeredAgentName.add(registered.getAgentName()));
                allAgents.forEach(newAgent -> allAgentName.add(newAgent.getAgentName()));
                registeredAgentName.forEach(registered -> {
                    if (!allAgentName.contains(registered)) {
                        leftAgents.add(registered);
                    }
                });
//                boolean isLeft = false;
//                for (DTOAgentDetails dtoAgentDetails1 : agentsRegisteredToAlly) {
//                    for (DTOAgentDetails dtoAgentDetails2 : allAgents) {
//                        if (dtoAgentDetails1.getAgentName().equals(dtoAgentDetails2.getAgentName())) {
//                            isLeft = true;
//                            break;
//                        }
//                    }
//                }
                messageForUserLabelTabDashboard.setText(leftAgents + "left the ally");
                messageForUserLabelTabDashboard.setTextFill(Color.RED);
            }
            agentsRegisteredToAlly = allAgents;
            ObservableList<DTOAgentDetails> items = agentDetailsTableView.getItems();
            items.clear();
            allAgents.forEach(dtoAgentDetails -> agentDetailsTableView.getItems().add(dtoAgentDetails));

        });
    }
    public void setManager(AllyManagerController allyManagerController){

        this.manager = allyManagerController;

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
        battleFieldNameDTOContestDataTabDashboard.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("battleFieldName"));
        uboatNameDTOContestDataTabDashboard.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("uboatName"));
        statusDTOContestDataTabDashboard.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("status"));
        levelDTOContestDataTabDashboard.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("level"));
        registeredAlliesDTOContestDataTabDashboard.setCellValueFactory(new PropertyValueFactory<DTOContestData, Integer>("numOfRegisteredAllies"));
        alliesNeededDTOContestDataTabDashboard.setCellValueFactory(new PropertyValueFactory<DTOContestData, Integer>("numOfAlliesNeeded"));

//        allContestsTableViewDTOContestDataTabDashboard.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DTOContestData>() {
//            @Override
//            public void changed(ObservableValue<? extends DTOContestData> observable, DTOContestData oldValue, DTOContestData newValue) {
//                isUboatSelected = true;
//                if (isMissionSizeSet && atLeastOneAgentInAlly) {
//                    readyButton.setDisable(false);
//                }
//                else {
//                    readyButton.setDisable(true);
//                }
//            }
//        });

        missionSizeTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (!newValue.isEmpty() && isInteger(newValue) && Integer.parseInt(newValue) > 0) {
                    isMissionSizeSet = true;
                    if (isUboatSelected && atLeastOneAgentInAlly) {
                        readyButton.setDisable(false);
                    } else {
                        readyButton.setDisable(true);
                    }

                } else {
                    isMissionSizeSet = false;
                    readyButton.setDisable(true);
                    if (!newValue.isEmpty()) {
                        invokeErrorAlert("Invalid number of mission size\nMission Size should be integer and greater than 0 ");
                    }
                }
            }
        });


        numOfThreadsColumn.setCellValueFactory(new PropertyValueFactory<DTOAgentDetails, Integer>("numOfThreads"));
        agentNameColumnDTOAgentDetails.setCellValueFactory(new PropertyValueFactory<DTOAgentDetails, String>("agentName"));
        numOfMissionsColumn.setCellValueFactory(new PropertyValueFactory<DTOAgentDetails, Integer>("numOfMissionPerRequest"));



        allyNameColumnDTOAllyTeamDetails.setCellValueFactory(new PropertyValueFactory<DTOAllyTeamDetails, String>("allyName"));
        numOfAgentsColumnDTOAllyTeamDetails.setCellValueFactory(new PropertyValueFactory<DTOAllyTeamDetails, Integer>("numOfAgents"));
        missionSizeColumnDTOAllyTeamDetails.setCellValueFactory(new PropertyValueFactory<DTOAllyTeamDetails, Integer>("missionSize"));

        numOfDoneMissionsDTOAgentsProgressData.setCellValueFactory(new PropertyValueFactory<DTOAgentsProgressData, Long>("numOfDoneMissions"));
        numOfCandidatesDTOAgentsProgressData.setCellValueFactory(new PropertyValueFactory<DTOAgentsProgressData, Integer>("numOfCandidates"));
        totalMissionsDTOAgentsProgressData.setCellValueFactory(new PropertyValueFactory<DTOAgentsProgressData, Long>("totalMissions"));
        agentNameColumnDTOAgentsProgressData.setCellValueFactory(new PropertyValueFactory<DTOAgentsProgressData, String>("agentName"));

//        battleFieldNameDTOContestDataTabContest.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("battleFieldName"));
//        uboatNameDTOContestDataTabContest.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("uboatName"));
//        statusDTOContestDataTabContest.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("status"));
//        levelDTOContestDataTabContest.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("level"));
//        registeredAlliesDTOContestDataTabContest.setCellValueFactory(new PropertyValueFactory<DTOContestData, Integer>("numOfRegisteredAllies"));
//        alliesNeededDTOContestDataTabContest.setCellValueFactory(new PropertyValueFactory<DTOContestData, Integer>("numOfAlliesNeeded"));

        agentNameDTODecryptionCandidate.setCellValueFactory(new PropertyValueFactory<DTODecryptionCandidate, String>("agentName"));
        configurationDTODecryptionCandidate.setCellValueFactory(new PropertyValueFactory<DTODecryptionCandidate, String>("configuration"));
        decryptionDTODecryptionCandidate.setCellValueFactory(new PropertyValueFactory<DTODecryptionCandidate, String>("decryption"));

//        allContestsTableViewDTOContestDataTabDashboard.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DTOContestData>() {
//            @Override
//            public void changed(ObservableValue<? extends DTOContestData> observable, DTOContestData oldValue, DTOContestData newValue) {
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (newValue != null) {
//                            chosenBattleField.setText(newValue.getBattleFieldName());
//                        }
//                    }
//                });
//            }
//        });
    }
}
