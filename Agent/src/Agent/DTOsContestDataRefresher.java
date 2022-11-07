package Agent;

import AgentManager.AgentManagerController;
import DTO.*;

import Http.HttpClientUtil;
import Utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class DTOsContestDataRefresher extends TimerTask{

    private final Consumer<DTOContestData> contestData;
    private final Consumer<List<DTOAgentDetails>> agentDetails;
    private final BooleanProperty shouldUpdate;
    private String agentName;
    private AgentManagerController manager;
    private boolean[] contestStarted;
    public DTOsContestDataRefresher(boolean[] contestStarted, AgentManagerController manager, BooleanProperty shouldUpdate,
                                    Consumer<DTOContestData> contestData,
                                    Consumer<List<DTOAgentDetails>> agentDetails) {
        this.manager = manager;
        this.agentName = manager.getUsername();
        this.shouldUpdate = shouldUpdate;
        this.contestData = contestData;
        this.agentDetails = agentDetails;
        this.contestStarted = contestStarted;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.GET_ALL_DTOs)
                .newBuilder()
                .addQueryParameter("agentName", agentName)
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

                        manager.getAgentController().askServerForLogout();
                    }
                    response.close();
                }
                else {
                    String jsonArrayOfDTOs = response.body().string();
                    Gson gson = new Gson();
                    Type type = new TypeToken<DTOAgentContestAllData>(){}.getType();
                    DTOAgentContestAllData dtoAgentContestAllData = gson.fromJson(jsonArrayOfDTOs, type);

                    contestData.accept(dtoAgentContestAllData.getDtoContestData());
                    agentDetails.accept(dtoAgentContestAllData.getDtoAgentDetailsList());
                    if (contestStarted[0] && (dtoAgentContestAllData.getContestStatus() == ContestStatus.FINISHED
                    || dtoAgentContestAllData.getContestStatus() == ContestStatus.UBOAT_LOGOUT)) {


                        manager.getAgentController().handleContestFinishedSituation(false);
                    }
                    else if (dtoAgentContestAllData.getContestStatus() == ContestStatus.ALLY_LOGOUT) {

                        manager.getAgentController().askServerForLogout();
                    }
                }
            }
        });
    }
}
