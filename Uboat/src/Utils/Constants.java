package Utils;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static int REFRESH_RATE = 1000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/UboatManager/uboatManager.fxml";
    public final static String UBOAT_PAGE_FXML_RESOURCE_LOCATION = "/Uboat/uboat.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/Login/loginUboat.fxml";
    //public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server_war";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String GET_TEAM_DETAILS = FULL_SERVER_PATH + "/uboatAllyTeamsDetails";
    public final static String GET_DECRYPTION_CANDIDATES= FULL_SERVER_PATH + "/uboatDecryptionCandidates";

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/uboatLogin";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";
    public final static String MACHINE_COMPONENTS = FULL_SERVER_PATH + "/machineComponents";
    public final static String DICTIONARY = FULL_SERVER_PATH + "/dictionary";
    public final static String XML_FILE = FULL_SERVER_PATH + "/xmlFile";
    public final static String GET_ALL_UBOATS = FULL_SERVER_PATH + "/allUboats";


    public final static String SET_CODE = FULL_SERVER_PATH + "/setCode";
    public final static String RANDOM_CODE = FULL_SERVER_PATH + "/randomCode";
    public final static String INPUT_PROCESSING = FULL_SERVER_PATH + "/inputProcessing";
    public final static String RESET_CODE = FULL_SERVER_PATH + "/resetCode";
    public final static String READY_FOR_CONTEST = FULL_SERVER_PATH + "/uboatReadyForContest";
    public final static String CONTEST_FINISHED_MESSAGE = FULL_SERVER_PATH + "/contestFinished";
    public final static String LOGOUT_UBOAT = FULL_SERVER_PATH + "/logoutUboat";
    public final static String GET_CONTEST_STATE = FULL_SERVER_PATH + "/getContestState";
    public final static String UBOAT_CONFIRM = FULL_SERVER_PATH + "/uboatConfirm";
    public final static String GET_WINNER = FULL_SERVER_PATH + "/getWinner";


//    // Text Fill Colors
//    public final static String GREEN = "#00d74f";
//    public final static String RED = "#ff0000";
//    public final static String BLACK = "#000000";
//    public final static String LIGHT_BLUE = "#00f6ff";



    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}