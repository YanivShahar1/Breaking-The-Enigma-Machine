package Uboat;

import DTO.*;
import Login.LoginUboat;
import Machine.*;
import Http.HttpClientUtil;
import SystemExceptions.*;
import UboatManager.UboatControllerManager;
import Utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

import static Utils.Constants.REFRESH_RATE;

public class Uboat implements Initializable {

    @FXML private Label messageForUserLabel;
    @FXML private Button confirmButton;
    @FXML private Label nameLabel;
    @FXML private Tab contestTab;
    @FXML private TabPane uboatTabPane;
    private LoginUboat loginUboat;
    private UboatControllerManager manager;
    private String allyNameWinner;
    private ContestStatus contestStatus;
    private int selectedReflectorId;
    private int numOfSelectedRotors;
    private String finalDecryption;
    private List<ComboBox<String>> rotorPositionsList;
    @FXML
    private HBox initRotorsHbox;
    @FXML
    private Button setCodeButton;
    @FXML
    private Button loadXMLButton;
    @FXML
    private Button randomCodeButton;
    @FXML
    private TextField filePath;
    @FXML
    private TextArea machineDetails;
    @FXML
    private TextField currentCodeConfigurationBoxTabContest;
    @FXML
    private TextField inputForEncryptionButtonTabContest;
    @FXML
    private TextField outputToDecryptionButtonTabContest;
    @FXML
    private ComboBox<Integer> rotorsComboBox;
    @FXML
    private Button processButtonTabContest;
    @FXML
    private Button resetCodeButton;
    @FXML
    private TextField searchField;
    @FXML
    private ListView dictionaryListView;
    @FXML
    private Button addButton;
    @FXML
    private Button addAllWordsButton;
    @FXML
    private HBox reflectorIDHbox;
    @FXML
    Button readyButton;
    @FXML Button clearButton;
    private Trie trie;
    private List<ComboBox<Integer>> optionalRotorsComboBoxList;
    private List<ToggleButton> optionalReflectorsButtonsList;
    private DTOMachineComponents dtoMachineComponents;
    private boolean[] isWinnerDetected;

    final boolean[] isXMLFileLoaded = {false};
    final boolean[] isCodeConfigurationSet = {false};
    final boolean[] areRotorsDisplayed = {false};
    final boolean[] areReflectorsDisplayed = {false};

    //Ally team details TableView with his columns
    @FXML private TableView<DTOAllyTeamDetails> allyTeamDetailsTableView;
    @FXML private TableColumn<DTOAllyTeamDetails, String> allyNameColumn;
    @FXML private TableColumn<DTOAllyTeamDetails, Integer> numOfAgentsColumn;
    @FXML private TableColumn<DTOAllyTeamDetails, Integer> missionSizeColumn;


    //Decryption Candidate TableView with his columns
    @FXML private TableView<DTODecryptionCandidate> decryptionCandidateTableView;
    @FXML private TableColumn<DTODecryptionCandidate, String> agentnameColumn;
    @FXML private TableColumn<DTODecryptionCandidate, String> decryptionColumn;
    @FXML private TableColumn<DTODecryptionCandidate, String> configurationColumn;

    private final BooleanProperty autoUpdate;
    private TimerTask candidatesListRefresher;
    private TimerTask allyTeamsListRefresher;
    private TimerTask isContestStartedRefresher;
    private Timer isContestStartedTimer;
    private Timer candidatesListTimer;
    private Timer allyTeamsListTimer;

    public Uboat() {
        allyNameColumn = new TableColumn<>();
        configurationColumn = new TableColumn<>();
        agentnameColumn = new TableColumn<>();
        decryptionColumn = new TableColumn<>();
        missionSizeColumn = new TableColumn<>();
        numOfAgentsColumn = new TableColumn<>();


        autoUpdate = new SimpleBooleanProperty(true);
        loginUboat = new LoginUboat();
        selectedReflectorId = -1;
        reflectorIDHbox = new HBox();
        rotorPositionsList = new ArrayList<>();
        initRotorsHbox = new HBox();
        rotorsComboBox = new ComboBox<>();
        filePath = new TextField();
        machineDetails = new TextArea();
        currentCodeConfigurationBoxTabContest = new TextField();
        dictionaryListView = new ListView<>();
        addButton = new Button();
        addAllWordsButton = new Button();
        processButtonTabContest = new Button();
        optionalReflectorsButtonsList = new ArrayList<>();
        optionalRotorsComboBoxList = new ArrayList<>();
        readyButton = new Button();

        isWinnerDetected = new boolean[1];
        isWinnerDetected[0] = false;

        nameLabel = new Label();
        messageForUserLabel = new Label();
        confirmButton = new Button();
        contestTab = new Tab();
        uboatTabPane = new TabPane();
    }

    public void resetAllUboatData() {
        allyNameWinner = "";
        selectedReflectorId = -1;
        numOfSelectedRotors = 0;
        finalDecryption = "";
        contestStatus = ContestStatus.WAITING;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                rotorPositionsList.clear();
                filePath.clear();
                machineDetails.clear();
                currentCodeConfigurationBoxTabContest.clear();
                rotorsComboBox.getItems().clear();
                searchField.clear();
                optionalRotorsComboBoxList.clear();
                optionalReflectorsButtonsList.clear();
            }
        });
        isWinnerDetected[0] = false;
        isXMLFileLoaded[0] = false;
        isCodeConfigurationSet[0] = false;
        areRotorsDisplayed[0] = false;
        areReflectorsDisplayed[0] = false;
    }
    public void setNameLabel(String nameLabel) {
        this.nameLabel.setText(nameLabel);
    }
    @FXML
    public void logoutButtonClicked() {

        String finalUrl = HttpUrl
                .parse(Constants.LOGOUT_UBOAT)
                .newBuilder()
                .addQueryParameter("uboatName", manager.getUsername())
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
                    stopAllyTeamsListRefresher();
                    stopCandidatesListRefresher();
                    stopAskingServerIfContestStarted();
                    resetAllUboatData();
                    resetAllUboatPage();
                    manager.switchToLoginScreen();
                    confirmButtonClicked();
                    response.close();
                }
            }
        });
    }
    public void resetAllUboatPage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadXMLButton.setDisable(false);
                setCodeButton.setDisable(true);
                randomCodeButton.setDisable(true);
                contestTab.setDisable(true);
                dictionaryListView.getItems().clear();
                initRotorsHbox.getChildren().clear();
                reflectorIDHbox.getChildren().clear();
                messageForUserLabel.setText("");
                clearInputOutput();
                clearTables();
                uboatTabPane.getSelectionModel().select(0);
            }
        });
    }
    @FXML
    public void loadXMLFileButton(ActionEvent e) throws IOException, ConnectionWithServerFailed {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(null);
        if (xmlFile != null) {
            final boolean[] errorOccurred = {false};
            final String[] errorDetails = {""};
            String finalUrl = HttpUrl
                    .parse(Constants.XML_FILE)
                    .newBuilder()
                    .addQueryParameter("uboatName", manager.getUsername())
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
                        invokeErrorAlert(responseBody);
                        response.close();
                    }
                    else {
                        filePath.setText(xmlFile.getAbsolutePath());
                        addButton.setDisable(false);
                        addAllWordsButton.setDisable(false);
                        loadXMLButton.setDisable(true);
                        randomCodeButton.setDisable(false);
                        try {
                            displayMachineComponents();
                            displayDictionary();
                            startTeamDetailsRefresher();
                        } catch (ConnectionWithServerFailed ex) {
                            throw new RuntimeException(ex);
                        }

                        inputForEncryptionButtonTabContest.clear();
                        outputToDecryptionButtonTabContest.clear();
                        currentCodeConfigurationBoxTabContest.clear();
                        response.close();
                    }
                }
            }, "loadXMLFileButton", xmlFile.getAbsolutePath());
            if (errorOccurred[0]) {
                throw new ConnectionWithServerFailed(errorDetails[0]);
            }
        }
    }
    public void invokeWinnerWindow(String allyName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                confirmButton.setDisable(false);
                messageForUserLabel.setTextFill(Color.GREEN);
                messageForUserLabel.setText("Winner: " + allyName + " Team!");
            }
        });
    }
    @FXML
    public void confirmButtonClicked() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageForUserLabel.setText("");
                confirmButton.setDisable(true);
                clearButton.setDisable(false);
                resetCodeButton.setDisable(false);
                processButtonTabContest.setDisable(false);
                //setCodeButton.setDisable(false);
                randomCodeButton.setDisable(false);
                clearInputOutput();
                clearTables();
            }
        });
        contestStatus = ContestStatus.WAITING;
        tellServerUboatConfirmed();
    }
    public void tellServerUboatConfirmed() {
        String finalUrl = HttpUrl
                .parse(Constants.UBOAT_CONFIRM)
                .newBuilder()
                .addQueryParameter("uboatName", manager.getUsername())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                response.close();
            }
        });
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
    public void displayOptionalReflectors() {
        if (areReflectorsDisplayed[0]) {
            return;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                optionalReflectorsButtonsList.add(null); // dummy
                int numOfReflectors = dtoMachineComponents.getNumOfOptionalReflectors();

                for (int i = 1; i <= numOfReflectors; i++) {
                    ToggleButton newReflectorButton = new ToggleButton(String.valueOf(i));
                    optionalReflectorsButtonsList.add(newReflectorButton);
                    reflectorIDHbox.getChildren().add(newReflectorButton);

                    int finalI = i;
                    int neededRotors = dtoMachineComponents.getNumOfUsedRotors();

                    //Button setCodeButton = this.setCodeButton;
                    newReflectorButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (newReflectorButton.isSelected()) {

                                if (selectedReflectorId == -1) { //didn't chose yet
                                    selectedReflectorId = finalI;
                                } else {
                                    optionalReflectorsButtonsList.get(selectedReflectorId).setSelected(false);
                                    selectedReflectorId = finalI;
                                }

                                if (numOfSelectedRotors >= neededRotors) {
                                    setCodeButton.setDisable(false);
                                } else {
                                    setCodeButton.setDisable(true);
                                }
                            } else {
                                selectedReflectorId = -1;
                                setCodeButton.setDisable(true);
                            }
                        }
                    });
                }
                areReflectorsDisplayed[0] = true;
            }
        });
    }
    public void displayOptionalRotors() {
        if (areRotorsDisplayed[0]) {
            return;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                List<Integer> allPossibleRotorsId = new ArrayList<>();
                for (int i = 1; i <= dtoMachineComponents.getNumOfOptionalRotors(); i++) {
                    allPossibleRotorsId.add(i);
                }
                optionalRotorsComboBoxList.add(null); // dummy
                rotorPositionsList.add(null);
                List<String> abcList = dtoMachineComponents.getAbcList();
                for (int i = 1; i <= dtoMachineComponents.getNumOfUsedRotors(); i++) {
                    VBox rotorVbox = new VBox();
                    ComboBox<Integer> newRotorComboBox = new ComboBox<>();
                    optionalRotorsComboBoxList.add(newRotorComboBox);

                    newRotorComboBox.getItems().addAll(allPossibleRotorsId);


                    rotorVbox.getChildren().add(newRotorComboBox);

                    ComboBox<String> newPosComboBox = new ComboBox<>();
                    newPosComboBox.getItems().addAll(abcList);
                    newPosComboBox.setValue(abcList.get(0)); // first letter in abc
                    newPosComboBox.setDisable(true);
                    rotorPositionsList.add(newPosComboBox);
                    rotorVbox.getChildren().add(newPosComboBox);
                    rotorVbox.setSpacing(3);
                    initRotorsHbox.getChildren().add(rotorVbox);

                    int neededRotors = dtoMachineComponents.getNumOfUsedRotors();
                    Button setCodeButtonTemp = setCodeButton;
                    newRotorComboBox.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            newPosComboBox.setDisable(false);
                            numOfSelectedRotors += 1;
                            if (numOfSelectedRotors >= neededRotors && selectedReflectorId != -1) {
                                setCodeButtonTemp.setDisable(false);
                            } else {
                                setCodeButtonTemp.setDisable(true);
                            }
                        }
                    });
                }
                areRotorsDisplayed[0] = true;
            }
        });
    }
    public void displayMachineComponents() throws ConnectionWithServerFailed {
        final boolean[] errorOccurred = {false};
        final String[] errorDetails = {""};
        String finalUrl = HttpUrl
                .parse(Constants.MACHINE_COMPONENTS)
                .newBuilder()
                .addQueryParameter("uboatName", manager.getUsername())
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
                    invokeErrorAlert(responseBody);
                } else {
                    Gson gson = new Gson();

                    dtoMachineComponents = gson.fromJson(response.body().string(), DTOMachineComponents.class);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            machineDetails.setText("");
                            printMachineComponents(dtoMachineComponents);
                            displayOptionalRotors();
                            displayOptionalReflectors();
                        }
                    });
                }
            }
        });

        if (errorOccurred[0]) {
            throw new ConnectionWithServerFailed(errorDetails[0]);
        }
    }
    private void printMachineComponents(DTOMachineComponents machineComponents) {
        currentCodeConfigurationBoxTabContest.setText("");
        machineDetails.appendText("Number of optional rotors (number of used rotors): "
                + machineComponents.getNumOfOptionalRotors() + " (" + machineComponents.getNumOfUsedRotors() + ")\n");
        machineDetails.appendText("Number of reflectors: " + machineComponents.getNumOfOptionalReflectors() + "\n");
        machineDetails.appendText("Number of processed messages so far: " + machineComponents.getNumOfProcessedMessages() + "\n");
        printStartingCodeConfiguration(machineComponents.getCurrent(), machineDetails);
        currentCodeConfigurationBoxTabContest.appendText(printCurrentCodeConfiguration(machineComponents.getCurrent()));
    }
    private void printStartingCodeConfiguration(DTOCodeConfigurationDescriptor codeConfigurationDescriptor, TextInputControl where) {
        printRotorsIDAndStartingWindowDistance(codeConfigurationDescriptor.getUsedRotors(), where);
        printRotorsStartingPositions(codeConfigurationDescriptor.getUsedRotors(), where);
        where.appendText(printReflectorID(codeConfigurationDescriptor.getUsedReflector()));
    }
    private String printCurrentCodeConfiguration(DTOCodeConfigurationDescriptor codeConfigurationDescriptor) {
        String str = "";
        str += printRotorsIDAndSCurrentWindowDistance(codeConfigurationDescriptor.getUsedRotors());
        str += printRotorsCurrentPositions(codeConfigurationDescriptor.getUsedRotors());
        str += printReflectorID(codeConfigurationDescriptor.getUsedReflector());
        return str;
    }
    private String printReflectorID(Reflector reflector) {
        String str = "";
        str += "<" + reflector.getId() + ">\n";
        return str;
    }
    private void printRotorsStartingPositions(List<Rotor> rotorsList, TextInputControl where) {
        where.appendText("<");
        for (int i = rotorsList.size() - 1; i >= 1; i--) {
            where.appendText(rotorsList.get(i).getStartingPosition());
        }
        where.appendText(">");
    }
    private String printRotorsCurrentPositions(List<Rotor> rotorsList) {
        String str = "";
        str += "<";
        for (int i = rotorsList.size() - 1; i >= 1; i--) {
            str += rotorsList.get(i).convertIndexToKey(1, Direction.RIGHT_TO_LEFT); //RIGHT_TO_LEFT
        }
        str += ">";
        return str;
    }
    private String printRotorsIDAndSCurrentWindowDistance(List<Rotor> rotorsList) {
        String str = "";
        str += "<";
        for (int i = rotorsList.size() - 1; i >= 1; i--) {
            if (i != rotorsList.size() - 1) {
                str += ",";
            }
            str += rotorsList.get(i).id + "(" + calcWindowCurrentDistanceFromNotch(rotorsList.get(i)) + ")";
        }
        str += ">";
        return str;
    }
    private void printRotorsIDAndStartingWindowDistance(List<Rotor> rotorsList, TextInputControl where) {
        where.appendText("<");
        for (int i = rotorsList.size() - 1; i >= 1; i--) {
            if (i != rotorsList.size() - 1) {
                where.appendText(",");
            }
            where.appendText(rotorsList.get(i).id + "(" + calcWindowStartingDistanceFromNotch(rotorsList.get(i)) + ")");
        }
        where.appendText(">");
    }
    private int calcWindowStartingDistanceFromNotch(Rotor rotor) {
        int distance = (rotor.getNotch() - rotor.getStartingNumOfRotations()) % rotor.getRotorSize() - 1;
        if (distance < 0) {
            distance = rotor.getRotorSize() + distance;
        }
        return distance;
    }
    private int calcWindowCurrentDistanceFromNotch(Rotor rotor) {
        int distance = (rotor.getNotch() - rotor.numOfRotations) % rotor.getRotorSize() - 1;
        if (distance < 0) {
            distance = rotor.getRotorSize() + distance;
        }
        return distance;
    }
    private void displayDictionary() throws ConnectionWithServerFailed {
        dictionaryListView.getItems().clear();
        final Set<String>[] dictionary = new Set[]{new HashSet<>()};
        final boolean[] errorOccurred = {false};
        final String[] errorDetails = {""};
        String finalUrl = HttpUrl
                .parse(Constants.DICTIONARY)
                .newBuilder()
                .addQueryParameter("uboatName", manager.getUsername())
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
                    invokeErrorAlert(responseBody);
                } else {
                    Gson gson = new Gson();
                    Type type = new TypeToken<Set<String>>() {
                    }.getType();
                    dictionary[0] = gson.fromJson(response.body().string(), type);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (String word : dictionary[0]) {
                                dictionaryListView.getItems().addAll(word + "\n");
                            }
                            trie = new Trie(dictionary[0]);
                        }
                    });
                }
            }
        });
        if (errorOccurred[0]) {
            throw new ConnectionWithServerFailed(errorDetails[0]);
        }
    }
    private void displayDictionary(Set<String> dictionary) {
        dictionaryListView.getItems().clear();

        for (String word : dictionary) {
            dictionaryListView.getItems().addAll(word + "\n");
        }
    }
    @FXML
    public void setCodeButton(ActionEvent e) throws ConnectionWithServerFailed {
        String[] configurationData = new String[3];  //(rotorsID, rotorsPos, reflectorID);
        getConfigurationData(configurationData);

        final boolean[] errorOccurred = {false};
        final String[] errorDetails = {""};
        String finalUrl = HttpUrl
                .parse(Constants.SET_CODE)
                .newBuilder()
                .addQueryParameter("uboatName", manager.getUsername())
                .addQueryParameter("rotorsID", configurationData[0])
                .addQueryParameter("rotorsPositions", configurationData[1])
                .addQueryParameter("reflectorID", configurationData[2])
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
                    invokeErrorAlert(responseBody);
                } else {
                    isCodeConfigurationSet[0] = true;
                    contestTab.setDisable(false);
                    uboatTabPane.getTabs().get(1).setDisable(false);
                    try {
                        displayMachineComponents();
                        processButtonTabContest.setDisable(false);
                        resetCodeButton.setDisable(false);
                        clearButton.setDisable(false);
                    } catch (ConnectionWithServerFailed ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        if (errorOccurred[0]) {
            throw new ConnectionWithServerFailed(errorDetails[0]);
        }

//        List<Integer> tempRotorsIDList = systemEngine.setUsedRotors(configurationData[0]);
//        List<Rotor> tempRotorsList = systemEngine.setStartingUsedRotorsPositions(configurationData[1], systemEngine.createUsedRotorsList(tempRotorsIDList));
//        Reflector tempReflector = systemEngine.setUsedReflector(configurationData[2]);
//        systemEngine.setCodeConfiguration(tempRotorsList, tempReflector, null);
//        systemEngine.setEnigmaMachine();
//        isCodeConfigurationSet = true;


        //this.originalEnigma = this.systemEngine.getEnigma();
        //clearInputOutput();
    }
    public void getConfigurationData(String[] configurationData) {
        // 0 = rotorsID, 1 = String rotorsPos,2 = String reflectorID
        //optionalRotorsButtonsList is with dummy head
        //int totalRotors = 0;
        //int neededRotors = dtoMachineComponents.getNumOfUsedRotors();
        int i;
        configurationData[0] = configurationData[1] = configurationData[2] = "";

        for (i = 1; i < optionalRotorsComboBoxList.size(); i++) {
            configurationData[0] += optionalRotorsComboBoxList.get(i).getValue();
            if (i != optionalRotorsComboBoxList.size() - 1) {
                configurationData[0] += ",";
            }
            String startingPosOfRotor = rotorPositionsList.get(i).getValue();
            configurationData[1] += startingPosOfRotor;
        }

        for (i = 1; i < dtoMachineComponents.getNumOfOptionalReflectors() + 1; i++) {
            if (optionalReflectorsButtonsList.get(i).isSelected()) {
                configurationData[2] += String.valueOf(i);
            }
        }
    }
    @FXML
    public void randomCodeButton(ActionEvent e) throws ConnectionWithServerFailed {
        final boolean[] errorOccurred = {false};
        final String[] errorDetails = {""};
        String finalUrl = HttpUrl
                .parse(Constants.RANDOM_CODE)
                .newBuilder()
                .addQueryParameter("uboatName", manager.getUsername())
                .addQueryParameter("isPlugin", "false")
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
                    invokeErrorAlert(responseBody);
                } else {
                    isCodeConfigurationSet[0] = true;
                    contestTab.setDisable(false);
                    uboatTabPane.getTabs().get(1).setDisable(false);
                    try {
                        clearInputOutput();
                        displayMachineComponents();
                        processButtonTabContest.setDisable(false);
                        resetCodeButton.setDisable(false);
                        clearButton.setDisable(false);
                    } catch (ConnectionWithServerFailed ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        if (errorOccurred[0]) {
            throw new ConnectionWithServerFailed(errorDetails[0]);
        }
    }
    @FXML
    public void clearInputOutput() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                inputForEncryptionButtonTabContest.clear();
                outputToDecryptionButtonTabContest.clear();
            }
        });
    }
    public void clearTables() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                decryptionCandidateTableView.getItems().clear();
                allyTeamDetailsTableView.getItems().clear();
            }
        });
    }
    @FXML
    public void inputProcessingButton(ActionEvent e) throws ConnectionWithServerFailed {
        String input = inputForEncryptionButtonTabContest.getText();
        if (input.length() > 0) {
            final boolean[] errorOccurred = {false};
            final String[] output = {""};
            final String[] errorDetails = {""};
            String finalUrl = HttpUrl
                    .parse(Constants.INPUT_PROCESSING)
                    .newBuilder()
                    .addQueryParameter("uboatName", manager.getUsername())
                    .addQueryParameter("input", input)
                    .addQueryParameter("isManual", "false")
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
                        invokeErrorAlert(responseBody);
                    } else {
                        output[0] = response.body().string();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                outputToDecryptionButtonTabContest.setText(output[0]);
                                try {
                                    displayMachineComponents();
                                } catch (ConnectionWithServerFailed ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
                    }
                }
            });
            if (errorOccurred[0]) {
                throw new ConnectionWithServerFailed(errorDetails[0]);
            }
        }
    }
    @FXML
    public void resetCodeConfigurationButton(ActionEvent e) throws ConnectionWithServerFailed {
        final boolean[] errorOccurred = {false};
        final String[] errorDetails = {""};
        String finalUrl = HttpUrl
                .parse(Constants.RESET_CODE)
                .newBuilder()
                .addQueryParameter("uboatName", manager.getUsername())
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
                    invokeErrorAlert(responseBody);
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            clearInputOutput();
                            try {
                                displayMachineComponents();
                            } catch (ConnectionWithServerFailed ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
            }
        });
        if (errorOccurred[0]) {
            throw new ConnectionWithServerFailed(errorDetails[0]);
        }
    }
    @FXML
    public void readyButtonClicked(ActionEvent e) throws ConnectionWithServerFailed {

        isWinnerDetected[0] = false;
        final boolean[] errorOccurred = {false};
        final String[] errorDetails = {""};
        String output = outputToDecryptionButtonTabContest.getText();
        String finalUrl = HttpUrl
                .parse(Constants.READY_FOR_CONTEST)
                .newBuilder()
                .addQueryParameter("uboatName", manager.getUsername())
                .addQueryParameter("output", output)
                .addQueryParameter("input", inputForEncryptionButtonTabContest.getText())
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
                    invokeErrorAlert(responseBody);
                }
                else {
                    processButtonTabContest.setDisable(true);
                    resetCodeButton.setDisable(true);
                    //setCodeButton.setDisable(true);
                    randomCodeButton.setDisable(true);
                    clearButton.setDisable(true);
                    readyButton.setDisable(true);
                    finalDecryption = inputForEncryptionButtonTabContest.getText();
                    contestStatus = ContestStatus.WAITING;
                    askServerIfContestStarted();
                    startCandidatesRefresher();
                }
            }
        });
        if (errorOccurred[0]) {
            throw new ConnectionWithServerFailed(errorDetails[0]);
        }
    }
    @FXML
    public void searchInDictionary() {
        Set<String> suggestions = trie.suggest(searchField.getText().toUpperCase());
        displayDictionary(suggestions);
    }
    @FXML
    public void addButton() {
        if (dictionaryListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        if (inputForEncryptionButtonTabContest.getText().equals("")) {
            inputForEncryptionButtonTabContest.appendText(dictionaryListView.getSelectionModel().getSelectedItem().toString());
        } else {
            inputForEncryptionButtonTabContest.appendText(" " + dictionaryListView.getSelectionModel().getSelectedItem().toString());
        }
    }
    @FXML
    public void addAllWords() {
        Set<String> allWords = this.trie.getAllWords();
        inputForEncryptionButtonTabContest.clear();
        allWords.forEach(word -> inputForEncryptionButtonTabContest.appendText(word + " "));
    }

    public TabPane getUboatTabPane() {
        return uboatTabPane;
    }

    public void setManager(UboatControllerManager uboatControllerManager) {
        this.manager = uboatControllerManager;
    }
    public void startTeamDetailsRefresher() {

        allyTeamsListRefresher = new TeamDetailsRefresher(
                manager.getUsername(),
                autoUpdate,
                this::updateTeamList);
        allyTeamsListTimer = new Timer();
        allyTeamsListTimer.schedule(allyTeamsListRefresher, 0, REFRESH_RATE);
    }
    public void startCandidatesRefresher() {

        candidatesListRefresher = new DecryptionsCandidatesRefresher(
                manager.getUsername(),
                autoUpdate,
                this::updateCandidatesList);
        candidatesListTimer = new Timer();
        candidatesListTimer.schedule(candidatesListRefresher, 0, REFRESH_RATE);
    }
    public void stopCandidatesListRefresher() {
        if (candidatesListTimer != null) {
            candidatesListTimer.cancel();
        }
        if (candidatesListRefresher != null) {
            candidatesListRefresher.cancel();
        }

    }
    public void stopAllyTeamsListRefresher() {
        if (allyTeamsListRefresher != null && allyTeamsListTimer != null) {
            allyTeamsListRefresher.cancel();
            allyTeamsListTimer.cancel();
        }

    }
    private void updateTeamList(List<DTOAllyTeamDetails> allTeams) {
        Platform.runLater(() -> {
            //System.out.println("allTeams --->> "+ allTeams);
            ObservableList<DTOAllyTeamDetails> items = allyTeamDetailsTableView.getItems();
            items.clear();
            allTeams.forEach(team ->allyTeamDetailsTableView.getItems().add(team));
            if ((contestStatus == ContestStatus.ALLY_LOGOUT || contestStatus == ContestStatus.FINISHED)
                    && allTeams.isEmpty() && confirmButton.isDisabled()) {
                messageForUserLabel.setText("All Allies Left.");
                messageForUserLabel.setTextFill(Color.RED);
                stopAskingServerIfContestStarted();
                contestStatus = ContestStatus.WAITING;
                confirmButton.setDisable(false);
            }
        });
    }
    public void askServerIfContestStarted() {

        isContestStartedRefresher = new TimerTask() {
            @Override
            public void run() {
                String finalUrl = HttpUrl
                        .parse(Constants.GET_CONTEST_STATE)
                        .newBuilder()
                        .addQueryParameter("uboatName", manager.getUsername())
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
                            Gson gson = new Gson();
                            String json = response.body().string();
                            contestStatus = gson.fromJson(json, ContestStatus.class);

                            if (contestStatus == ContestStatus.RUNNING) {
                                Platform.runLater(() -> {
                                    messageForUserLabel.setText("Contest Started");
                                    messageForUserLabel.setTextFill(Color.BLUE);
                                });
                            }
                        }
                    }
                });
            }
        };
        isContestStartedTimer = new Timer();
        isContestStartedTimer.schedule(isContestStartedRefresher, 0, REFRESH_RATE);
    }
    public void stopAskingServerIfContestStarted() {
        if (isContestStartedTimer != null && isContestStartedRefresher != null) {
            isContestStartedTimer.cancel();
            isContestStartedRefresher.cancel();
        }

    }
    private void updateCandidatesList(List<DTODecryptionCandidate> allCandidates) {
        if (isWinnerDetected[0]) {
            return;
        }
        Platform.runLater(() -> {
            ObservableList<DTODecryptionCandidate> items = decryptionCandidateTableView.getItems();
            items.clear();
            allCandidates.forEach(team ->decryptionCandidateTableView.getItems().add(team));
            for (DTODecryptionCandidate dtoDecryptionCandidate : allCandidates) {


                if (!isWinnerDetected[0] && dtoDecryptionCandidate.getDecryption().trim().equals(finalDecryption.trim())) {
                    stopCandidatesListRefresher();
                    isWinnerDetected[0] = true;
                    contestStatus = ContestStatus.FINISHED;
//                    tellServerContestFinished();
                    //allyNameWinner = dtoDecryptionCandidate.getAllyName();
//                    invokeWinnerWindow(allyNameWinner = dtoDecryptionCandidate.getAllyName());
                    getTheContestAllyNameWinner();
                    break;
                }
            }
        });
    }
    public void getTheContestAllyNameWinner() {

        String finalUrl = HttpUrl
                .parse(Constants.GET_WINNER)
                .newBuilder()
                .addQueryParameter("userType", "uboat")
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
                } else {
                    String winnerAlly = response.body().string();

                    invokeWinnerWindow(winnerAlly);
                    stopAskingServerIfContestStarted();
                }
            }
        });
    }

//    public void tellServerContestFinished() {
//        System.out.println("telling server contest finished.");
//        String finalUrl = HttpUrl
//                .parse(Constants.CONTEST_FINISHED_MESSAGE)
//                .newBuilder()
//                .addQueryParameter("uboatName", manager.getUsername())
//                .addQueryParameter("allyName", allyNameWinner)
//                .build()
//                .toString();
//
//        HttpClientUtil.runAsync(finalUrl, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                processButtonTabContest.setDisable(false);
//                resetCodeButton.setDisable(false);
//                setCodeButton.setDisable(false);
//                randomCodeButton.setDisable(false);
//                clearButton.setDisable(false);
//                response.close();
//            }
//        });
//    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        outputToDecryptionButtonTabContest.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String missionSize, String newMissionSize) {
                if (outputToDecryptionButtonTabContest.getText().isEmpty()) {
                    readyButton.setDisable(true);
                } else {
                    readyButton.setDisable(false);
                }
            }
        });

        allyNameColumn.setCellValueFactory(new PropertyValueFactory<DTOAllyTeamDetails, String>("allyName"));
        numOfAgentsColumn.setCellValueFactory(new PropertyValueFactory<DTOAllyTeamDetails, Integer>("numOfAgents"));
        missionSizeColumn.setCellValueFactory(new PropertyValueFactory<DTOAllyTeamDetails, Integer>("missionSize"));


        agentnameColumn.setCellValueFactory(new PropertyValueFactory<DTODecryptionCandidate, String>("agentName"));
        decryptionColumn.setCellValueFactory(new PropertyValueFactory<DTODecryptionCandidate, String>("decryption"));
        configurationColumn.setCellValueFactory(new PropertyValueFactory<DTODecryptionCandidate, String>("configuration"));

    }
}