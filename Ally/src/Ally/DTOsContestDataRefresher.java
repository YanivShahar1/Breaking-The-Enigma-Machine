package Ally;

import AllyManager.AllyManagerController;
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
    private final Consumer<List<DTOAllyTeamDetails>> contestsTeams;
    private final Consumer<List<DTOAgentsProgressData>> teamsAgentsProgressData;
    private final Consumer<List<DTODecryptionCandidate>> teamDecryptionCandidates;
    private final BooleanProperty shouldUpdate;
    private String allyName;
    private AllyManagerController manager;
    private boolean[] isWinnerWindowInvoked;
    public DTOsContestDataRefresher(boolean[] isWinnerWindowInvoked,
                                    AllyManagerController manager, BooleanProperty shouldUpdate,
                                    Consumer<DTOContestData> contestData,
                                    Consumer<List<DTOAllyTeamDetails>> contestsTeams,
                                    Consumer<List<DTOAgentsProgressData>> teamsAgentsProgressData,
                                    Consumer<List<DTODecryptionCandidate>> teamDecryptionCandidates) {
        this.isWinnerWindowInvoked = isWinnerWindowInvoked;
        this.manager = manager;
        this.allyName = manager.getUsername();
        this.shouldUpdate = shouldUpdate;
        this.contestData = contestData;
        this.contestsTeams = contestsTeams;
        this.teamsAgentsProgressData = teamsAgentsProgressData;
        this.teamDecryptionCandidates = teamDecryptionCandidates;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.GET_ALL_DTOs)
                .newBuilder()
                .addQueryParameter("allyName", allyName)
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
                    String jsonArrayOfDTOs = response.body().string();
                    Gson gson = new Gson();
                    Type type = new TypeToken<DTOAllyContestTabAllData>(){}.getType();
                    DTOAllyContestTabAllData dtoAllyContestTabAllData = gson.fromJson(jsonArrayOfDTOs, type);
                    contestData.accept(dtoAllyContestTabAllData.getDTOContestData());
                    contestsTeams.accept(dtoAllyContestTabAllData.getDTOAllyTeamDetails());
                    teamsAgentsProgressData.accept(dtoAllyContestTabAllData.getDtoAgentsProgressData());
                    teamDecryptionCandidates.accept(dtoAllyContestTabAllData.getDTODecryptionCandidate());

                    if (dtoAllyContestTabAllData.getDTOContestData() != null) {

                        if (dtoAllyContestTabAllData.getDTOContestData().getStatusAsContestStatus() == ContestStatus.FINISHED
                                || dtoAllyContestTabAllData.getDTOContestData().getStatusAsContestStatus() == ContestStatus.UBOAT_LOGOUT) {

                            manager.getAllyController().stopAskingServerForDTOsContestData();
                            manager.getAllyController().getTheContestAllyNameWinner();
                        }
                    }
                    else {
                        manager.getAllyController().stopAskingServerForDTOsContestData();
                        manager.getAllyController().getTheContestAllyNameWinner();
                    }
                }
            }
        });
    }
}
