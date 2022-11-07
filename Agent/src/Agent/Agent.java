package Agent;

import AgentManager.AgentManagerController;
import DTO.*;
import Http.HttpClientUtil;
import Utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

import static Utils.Constants.REFRESH_RATE;

public class Agent implements Initializable {

    @FXML
    Label nameLabel;
    @FXML private Button logoutButton;
    @FXML private TextField allyName;
    private AgentManagerController manager;
    private Timer isEveryoneReadytimer;
    private TimerTask agentAskReadyRefresher;
    private Timer isAllyApprovedGameOverTimer;
    private TimerTask isAllyApprovedGameOverRefresher;
    private final BooleanProperty autoUpdate;
    private boolean[] contestFinished;
    private boolean[] contestStarted;
    private boolean isWinnerInvoked = false;
    private CustomThreadPoolExecutor customThreadPoolExecutor;
    private BlockingQueue<Runnable> blockingQueue;
    private DecryptionsCandidatesQueue decryptionsCandidatesQueue;
    private Set<String> allDecryptionsTillNow;
    private String[] stateOfThreadPool;
    private boolean[] shouldAsking;
    private boolean[] sendCandidatesThreadIsUp;
    private boolean isAllyApprovedGameOver;

    // Agent Details
    @FXML private TableView<DTOAgentDetails> agentDetailsTableView;
    @FXML private TableColumn<DTOAgentDetails, String> agentNameColumnDTOAgentDetails;
    @FXML private TableColumn<DTOAgentDetails, Integer> numOfThreadsColumn;
    @FXML private TableColumn<DTOAgentDetails, Integer> numOfMissionsPerRequestColumn;

    // Contest Data
    @FXML private TableView<DTOContestData> contestTableViewDTOContestData;
    @FXML private TableColumn<DTOContestData, String> uboatNameDTOContestData;
    @FXML private TableColumn<DTOContestData, String> battleFieldNameDTOContestData;
    @FXML private TableColumn<DTOContestData, String> statusDTOContestData;
    @FXML private TableColumn<DTOContestData, Integer> registeredAlliesDTOContestData;
    @FXML private TableColumn<DTOContestData, Integer> alliesNeededDTOContestData;
    @FXML private TableColumn<DTOContestData, String> levelDTOContestData;

    // Agent progress Data
    @FXML private TableView<DTOAgentsProgressData> agentsProgressDataTableView;
    @FXML private TableColumn<DTOAgentsProgressData, Long> numOfDoneMissionsDTOAgentsProgressData;
    @FXML private TableColumn<DTOAgentsProgressData, Long> pulledMissionsDTOAgentsProgressData;
    @FXML private TableColumn<DTOAgentsProgressData, Integer> numOfCandidatesDTOAgentsProgressData;

    // Decryption Candidate
    @FXML private TableView<DTODecryptionCandidate> agentCandidatesDTODecryptionCandidateTableView;
    @FXML private TableColumn<DTODecryptionCandidate, String> decryptionDTODecryptionCandidate;
    @FXML private TableColumn<DTODecryptionCandidate, String> configurationDTODecryptionCandidate;

    private TimerTask DTOContestDataRefresher;
    private Timer DTOContestDataTimer;
    private TimerTask agentProgressDataRefresher;
    private Timer agentProgressDataTimer;
    private MissionsCounterListener missionsCounterListener;
    public Agent() {
        autoUpdate = new SimpleBooleanProperty(true);
//        isEveryoneReadytimer = new Timer();
        blockingQueue = new LinkedBlockingQueue<>();
        decryptionsCandidatesQueue = new DecryptionsCandidatesQueue();
        contestFinished = new boolean[1];
        contestFinished[0] = false;
        contestStarted = new boolean[1];
        contestStarted[0] = false;
        missionsCounterListener = new MissionsCounterListener(contestFinished);
        allDecryptionsTillNow = new HashSet<>();
        this.stateOfThreadPool = new String[1];
        this.stateOfThreadPool[0] = "Running";
        shouldAsking = new boolean[1];
        sendCandidatesThreadIsUp = new boolean[1];
        isAllyApprovedGameOver = false;
        shouldAsking[0] = false;
        sendCandidatesThreadIsUp[0] = false;
        nameLabel = new Label();
        logoutButton = new Button();
        allyName = new TextField();
        agentsProgressDataTableView = new TableView<>();
        agentCandidatesDTODecryptionCandidateTableView = new TableView<>();
        contestTableViewDTOContestData = new TableView<>();
        agentDetailsTableView = new TableView<>();

        //setThreadPool();
    }

    public Button getLogoutButton() {
        return logoutButton;
    }
    @FXML
    public void logoutButtonClicked(ActionEvent event) {

        askServerForLogout();
    }
    public void setNameLabel(String nameLabel) {
        this.nameLabel.setText(nameLabel);
    }

    public void setManager(AgentManagerController agentManagerController){
        this.manager = agentManagerController;
    }

    public void setThreadPool() {

        blockingQueue = new LinkedBlockingQueue<>();
        this.customThreadPoolExecutor =
                new CustomThreadPoolExecutor(manager.getNumOfThreads(),manager.getNumOfThreads(), 5,
                        TimeUnit.SECONDS, blockingQueue, new ThreadPoolExecutor.AbortPolicy());

        this.customThreadPoolExecutor.prestartAllCoreThreads();

    }


    public void setContestFinished(boolean contestFinished) {
        this.contestFinished[0] = contestFinished;
    }

    public void askServerIfEveryoneIsReady() {
        isWinnerInvoked = false;
        agentAskReadyRefresher = new AgentAskReadyRefresher(contestStarted, manager,
                autoUpdate, manager.getUsername());

        isEveryoneReadytimer = new Timer();
        isEveryoneReadytimer.schedule(agentAskReadyRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setShouldAsking(boolean shouldAsking) {
        this.shouldAsking[0] = shouldAsking;
    }

    public void stopAskingServerIfEveryoneIsReady() {
        if (isEveryoneReadytimer != null && agentAskReadyRefresher != null) {
            isEveryoneReadytimer.cancel();
            agentAskReadyRefresher.cancel();
        }
        setThreadPool();
        this.stateOfThreadPool[0] = "Running";
        contestStarted[0] = true;
//        missionsCounterListener.setNumOfMissionsPerRequest(manager.getNumOfMissionsPerRequest());
    }
    public void setAllyName(String allyName) {
        this.allyName.setText(allyName);
    }
    public void askServerForMissions() {
        if (contestFinished[0]) {
            return;
        }
//        System.out.println("in function -> askServerForMissions ");
//        if (!shouldAsking[0]) {
//            return;
//        }
//        System.out.println("should asking");


        //ask the server for new missions

        String finalUrl = HttpUrl
                .parse(Constants.GET_MISSIONS)
                .newBuilder()
                .addQueryParameter("agentName", manager.getUsername())
                .build()
                .toString();

        //shouldAsking[0] = false;
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //System.out.println("code = " + response.code());
                if (response.code() != 200) {
                    if (response.code() == 409) {

                        askServerForLogout();
                    }
                    response.close();

                } else {
//                    System.out.println("good response for GET MISSIONS");
                    String jsonArrayOfDTOMissions = response.body().string();
                    //System.out.println("missions from server = " + jsonArrayOfDTOMissions);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<DTOMission>>() {}.getType();
                    List<DTOMission> missionDataList = gson.fromJson(jsonArrayOfDTOMissions, type);

//                    System.out.println("Agent " + manager.getUsername() + "missionDataList size is : " + missionDataList.size());
                    if (missionDataList.size() != 0) {
//                        System.out.println("missionDataList size is " + missionDataList.size());
                        missionsCounterListener.setNumOfMissionsPerRequest(missionDataList.size());
                        missionsCounterListener.setTotalMissionPull();
                        //sendCandidatesWhenFinisedAllMissions();
                        for (DTOMission dto : missionDataList) {
                            try {
                                if (dto.getContestStatus() == ContestStatus.FINISHED ||
                                        dto.getContestStatus() == ContestStatus.UBOAT_LOGOUT) {
                                    handleContestFinishedSituation(false);
                                }
                                else {
                                    blockingQueue.put(new Mission(manager, dto.getEnigma(), dto.getAllyName(),
                                            dto.getMissionSize(), dto.getMessage(),
                                            manager.getUsername(), dto.getDictionary(),
                                            decryptionsCandidatesQueue, allDecryptionsTillNow,
                                            stateOfThreadPool, missionsCounterListener));
                                }

                            } catch (InterruptedException | CloneNotSupportedException e) {
                                throw new RuntimeException(e);
                            }

                        }

                    }
                    else {
                        shouldAsking[0] = true;
                    }
                }
            }
        });
    }

    public void askServerForMissionsRefresher() {

        askServerForMissions();
        //shouldAsking[0] = false;
        Thread missionsRefresherThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!contestFinished[0] && contestStarted[0]) {
                    try {
                        if (missionsCounterListener.getNumOfMissionsPerRequest() == 0 && shouldAsking[0]) {
                            shouldAsking[0] = false;
                            askServerForMissions();
                        }
                        else if (missionsCounterListener.waitUntilFinishAllMissions()) {
                            missionsCounterListener.resetMissionsCounter();
                            shouldAsking[0] = false;
                            askServerForMissions();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        missionsRefresherThread.start();
    }
    public void sendCandidatePerMission() {
        Thread candidateSenderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DTODecryptionCandidate dtoDecryptionCandidate;
                try {
                    dtoDecryptionCandidate = decryptionsCandidatesQueue.dequeue();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (dtoDecryptionCandidate != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(dtoDecryptionCandidate);

                    String finalUrl = HttpUrl
                            .parse(Constants.SEND_CANDIDATES)
                            .newBuilder()
                            .addQueryParameter("agentName", manager.getUsername())
                            .build()
                            .toString();

                    HttpClientUtil.runAsync(finalUrl, new Callback() {

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                            if (response.code() != 200) {
                                if (response.code() == 409) {

                                    askServerForLogout();
                                }
                                response.close();

                            } else {
                                Boolean isWinner = gson.fromJson(response.body().string(), Boolean.class);

                                if (isWinner) {
                                    //found winner in our candidates
                                    handleContestFinishedSituation(false);
                                    response.close();//?

                                }
                            }
                        }
                    }, "sendCandidates", json);
                }
            }
        });
        candidateSenderThread.start();
    }
    private void updateAgentsDetails(List<DTOAgentDetails> allAgents) {
        if(allAgents==null){
            return;
        }
        Platform.runLater(() -> {
            ObservableList<DTOAgentDetails> items = agentDetailsTableView.getItems();
            items.clear();
            allAgents.forEach(dtoAgentDetails -> agentDetailsTableView.getItems().add(dtoAgentDetails));
        });
    }
    public void updateAgentProgressDataRefresher() {
        agentProgressDataRefresher = new TimerTask() {
            @Override
            public void run() {
                updateAgentProgressData();
            }
        };
        agentProgressDataTimer = new Timer();
        agentProgressDataTimer.schedule(agentProgressDataRefresher, 0, 1000);
    }
    public void stopUpdatingAgentProgressData() {
        if (agentProgressDataTimer != null && agentProgressDataRefresher != null) {
            agentProgressDataTimer.cancel();
            agentProgressDataRefresher.cancel();
        }
    }
    public void updateAgentProgressData() {
        DTOAgentsProgressData dtoAgentsProgressData = new DTOAgentsProgressData(
                manager.getUsername(),
                missionsCounterListener.getMissionsDone(),
                missionsCounterListener.getTotalMissionPull(),
                missionsCounterListener.getNumOfCandidates()
        );
        Platform.runLater(() -> {
            agentsProgressDataTableView.getItems().clear();
            agentsProgressDataTableView.getItems().add(dtoAgentsProgressData);
            sendAgentProgressDataToServer(dtoAgentsProgressData);
        });
    }
    public void sendAgentProgressDataToServer(DTOAgentsProgressData dtoAgentsProgressData) {
        String finalUrl = HttpUrl
                .parse(Constants.UPDATE_AGENT_PROGRESS_DATA)
                .newBuilder()
                .addQueryParameter("agentName", manager.getUsername())
                .build()
                .toString();
        Gson gson = new Gson();
        String json = gson.toJson(dtoAgentsProgressData);

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    response.close();

                }
            }
        }, "sendAgentProgressDataToServer", json);
    }
    public void updateContestData(DTOContestData contestData) {
        Platform.runLater(() -> {
            ObservableList<DTOContestData> items = contestTableViewDTOContestData.getItems();
            items.clear();
            items.add(contestData);
        });
    }
    public void updateTeamsAgentsProgressData(DTOAgentsProgressData agentsProgressData) {
        Platform.runLater(() -> {
            ObservableList<DTOAgentsProgressData> items = agentsProgressDataTableView.getItems();
            items.clear();
            items.add(agentsProgressData);
        });
    }
    public void updateDecryptionCandidatesTableView(DTODecryptionCandidate dtoDecryptionCandidate) {
        Platform.runLater(() -> agentCandidatesDTODecryptionCandidateTableView.getItems().add(dtoDecryptionCandidate));
    }
    public void updateTeamDecryptionCandidates(List<DTODecryptionCandidate> decryptionCandidates) {
        Platform.runLater(() -> {
            ObservableList<DTODecryptionCandidate> items = agentCandidatesDTODecryptionCandidateTableView.getItems();
            items.clear();
            decryptionCandidates.forEach(dtoDecryptionCandidate -> agentCandidatesDTODecryptionCandidateTableView.getItems().add(dtoDecryptionCandidate));
        });
    }
    public void askServerForDTOsContestData() {
        DTOContestDataRefresher = new DTOsContestDataRefresher(
                contestStarted,
                manager,
                autoUpdate,
                this::updateContestData,
                this::updateAgentsDetails);
        DTOContestDataTimer = new Timer();
        DTOContestDataTimer.schedule(DTOContestDataRefresher, 0, REFRESH_RATE);
    }
    public void stopAskingServerForDTOsContestData() {
        if (DTOContestDataTimer != null && DTOContestDataRefresher != null) {
            DTOContestDataTimer.cancel();
            DTOContestDataRefresher.cancel();
        }
    }
    public void askServerIfAllyApprovedGameOverRefresher() {
        isAllyApprovedGameOverRefresher = new TimerTask() {
            @Override
            public void run() {
                String finalUrl = HttpUrl
                        .parse(Constants.ALLY_APPROVE_GAME_OVER)
                        .newBuilder()
                        .addQueryParameter("agentName", manager.getUsername())
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
                            isAllyApprovedGameOver = response.body().string().equals("true");
                            if (isAllyApprovedGameOver) {
                                agentCandidatesDTODecryptionCandidateTableView.getItems().clear();
                                agentsProgressDataTableView.getItems().clear();
                                stopAskingServerIfAllyApprovedGameOver();
                                resetAllAgentPage();
                                resetAllAgentData();
                                askServerIfEveryoneIsReady();
                            }

                        }
                    }
                });
            }
        };
        isAllyApprovedGameOverTimer = new Timer();
        isAllyApprovedGameOverTimer.schedule(isAllyApprovedGameOverRefresher, 0, REFRESH_RATE);
    }
    public void stopAskingServerIfAllyApprovedGameOver() {
        if (isAllyApprovedGameOverTimer != null && isAllyApprovedGameOverRefresher != null) {
            isAllyApprovedGameOverTimer.cancel();
            isAllyApprovedGameOverRefresher.cancel();
        }

    }
    public void handleContestFinishedSituation(boolean contestFinishedForcedLogout) {
        if (!isWinnerInvoked) {
            contestFinished[0] = true;
            contestStarted[0] = false;
            missionsCounterListener.setContestFinished(contestFinished);
            //shouldAsking[0] = false;

            if (customThreadPoolExecutor != null) {
                customThreadPoolExecutor.shutDownThreadPool();
            }


            stateOfThreadPool[0] = "Shutdown";
            decryptionsCandidatesQueue.clear();
            allDecryptionsTillNow.clear();
            if (!contestFinishedForcedLogout) {
                askServerIfAllyApprovedGameOverRefresher();
            }
            else {
                resetAllAgentData();
                resetAllAgentPage();
            }
            isWinnerInvoked = true;
            stopUpdatingAgentProgressData();
            updateAgentProgressData();
            logoutButton.setDisable(false);
        }

//                                    askServerIfEveryoneIsReady();
    }
    public void askServerForLogout() {

        String finalUrl = HttpUrl
                .parse(Constants.LOGOUT_AGENT)
                .newBuilder()
                .addQueryParameter("agentName", manager.getUsername())
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
                    handleContestFinishedSituation(true);
                    stopAskingServerForDTOsContestData();
                    stopAskingServerIfEveryoneIsReady();
                    manager.switchToLoginScreen();
                    manager.getLoginController().startAlliesListRefresher();
                    resetAllAgentPage();
                    resetAllAgentData();

                }
            }
        });
    }
    public void resetAllAgentData() {
        missionsCounterListener.resetCounterListener();
    }
    public void resetAllAgentPage() {
        Platform.runLater(() -> {
            agentCandidatesDTODecryptionCandidateTableView.getItems().clear();
            agentDetailsTableView.getItems().clear();
            agentsProgressDataTableView.getItems().clear();
            contestTableViewDTOContestData.getItems().clear();
            manager.getLoginController().clearAlliesOptions();
            allyName.clear();
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        battleFieldNameDTOContestData.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("battleFieldName"));
        uboatNameDTOContestData.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("uboatName"));
        statusDTOContestData.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("status"));
        levelDTOContestData.setCellValueFactory(new PropertyValueFactory<DTOContestData, String>("level"));
        registeredAlliesDTOContestData.setCellValueFactory(new PropertyValueFactory<DTOContestData, Integer>("numOfRegisteredAllies"));
        alliesNeededDTOContestData.setCellValueFactory(new PropertyValueFactory<DTOContestData, Integer>("numOfAlliesNeeded"));

        numOfThreadsColumn.setCellValueFactory(new PropertyValueFactory<DTOAgentDetails, Integer>("numOfThreads"));
        agentNameColumnDTOAgentDetails.setCellValueFactory(new PropertyValueFactory<DTOAgentDetails, String>("agentName"));
        numOfMissionsPerRequestColumn.setCellValueFactory(new PropertyValueFactory<DTOAgentDetails, Integer>("numOfMissionPerRequest"));

        numOfDoneMissionsDTOAgentsProgressData.setCellValueFactory(new PropertyValueFactory<DTOAgentsProgressData, Long>("numOfDoneMissions"));
        numOfCandidatesDTOAgentsProgressData.setCellValueFactory(new PropertyValueFactory<DTOAgentsProgressData, Integer>("numOfCandidates"));
        pulledMissionsDTOAgentsProgressData.setCellValueFactory(new PropertyValueFactory<DTOAgentsProgressData, Long>("totalMissions"));

        configurationDTODecryptionCandidate.setCellValueFactory(new PropertyValueFactory<DTODecryptionCandidate, String>("configuration"));
        decryptionDTODecryptionCandidate.setCellValueFactory(new PropertyValueFactory<DTODecryptionCandidate, String>("decryption"));

    }
}