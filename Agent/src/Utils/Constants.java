package Utils;

import com.google.gson.Gson;

public class Constants {
    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static int REFRESH_RATE = 1000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";


    // fxml locations
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/Login/loginAgent.fxml";
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/AgentManager/YABA.fxml";
    public final static String AGENT_PAGE_FXML_RESOURCE_LOCATION = "/Agent/agent.fxml";
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server_war";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/agentLogin";

    public final static String GET_ALL_ALLIES = FULL_SERVER_PATH + "/getAllAllies";
    public final static String IS_EVERYONE_READY = FULL_SERVER_PATH + "/isEveryoneReady";
    public final static String GET_MISSIONS = FULL_SERVER_PATH + "/getMissions";
    public final static String SEND_CANDIDATES = FULL_SERVER_PATH + "/sendCandidates";
    public final static String GET_TEAM_DETAILS = FULL_SERVER_PATH + "/agentTeamDetails";
    public final static String GET_ALL_DTOs = FULL_SERVER_PATH + "/allDTOsToAgent";
    public final static String ALLY_APPROVE_GAME_OVER = FULL_SERVER_PATH + "/allyApproveGameOverAgent";
    public final static String LOGOUT_AGENT = FULL_SERVER_PATH + "/logoutAgent";
    public final static String UPDATE_AGENT_PROGRESS_DATA = FULL_SERVER_PATH + "/updateAgentProgressData";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
