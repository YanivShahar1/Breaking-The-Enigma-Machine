package Agent;

import AgentManager.AgentManagerController;
import DTO.DTOContestData;
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
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AgentAskReadyRefresher extends TimerTask{
    private final BooleanProperty shouldUpdate;
    private String agentName;
    private AgentManagerController manager;
    private boolean[] contestStarted;
    public AgentAskReadyRefresher(boolean[] contestStarted, AgentManagerController manager,
                                  BooleanProperty shouldUpdate, String agentName) {

        this.shouldUpdate = shouldUpdate;
        this.agentName = agentName;
        this.manager = manager;
        this.contestStarted = contestStarted;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.IS_EVERYONE_READY)
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

                    manager.getAgentController().getLogoutButton().setDisable(true);
                    manager.getAgentController().stopAskingServerIfEveryoneIsReady();
                    manager.getAgentController().setShouldAsking(true);
                    manager.getAgentController().setContestFinished(false);
                    //manager.getAgentController().askServerForMissions();
                    manager.getAgentController().updateAgentProgressDataRefresher();
                    manager.getAgentController().askServerForMissionsRefresher();
                    response.close();

                }
            }
        });
    }
}