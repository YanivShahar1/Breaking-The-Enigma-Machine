package Utils;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static int REFRESH_RATE = 1000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";


    // fxml locations
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/Login/loginAlly.fxml";
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/AllyManager/allyManager.fxml";
    public final static String ALLY_PAGE_FXML_RESOURCE_LOCATION = "/Ally/ally.fxml";
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server_war";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/allyLogin";
    public final static String READY_FOR_CONTEST = FULL_SERVER_PATH + "/allyReadyForContest";

    public final static String GET_ALL_CONTESTS_DATA = FULL_SERVER_PATH + "/allContests";
    public final static String ASK_IF_THERE_IS_WINENR = FULL_SERVER_PATH + "/allyAskIsWinner";
    public final static String GET_ALL_DTOs = FULL_SERVER_PATH + "/allDTOsToAlly";
    public final static String GET_TEAM_DETAILS = FULL_SERVER_PATH + "/agentTeamDetails";
    public final static String GET_WINNER = FULL_SERVER_PATH + "/getWinner";
    public final static String REMOVE_ALLY_FROM_CONTEST = FULL_SERVER_PATH + "/removeAllyFromContest";
    public final static String GAME_OVER_APPROVED = FULL_SERVER_PATH + "/allyApprovedGameOverAlly";
    public final static String LOGOUT_ALLY = FULL_SERVER_PATH + "/logoutAlly";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}