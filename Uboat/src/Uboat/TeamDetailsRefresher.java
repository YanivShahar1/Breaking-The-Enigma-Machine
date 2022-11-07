package Uboat;

import DTO.DTOAllyTeamDetails;
import Http.HttpClientUtil;
import UboatManager.UboatControllerManager;
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

public class TeamDetailsRefresher extends TimerTask{

    private final Consumer<List<DTOAllyTeamDetails>> teamsDetailsConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;
    private String uboatName;

    public TeamDetailsRefresher(String uboatName, BooleanProperty shouldUpdate, Consumer<List<DTOAllyTeamDetails>> teamsDetailsConsumer) {

        this.uboatName = uboatName;
        this.shouldUpdate = shouldUpdate;
        this.teamsDetailsConsumer = teamsDetailsConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {

            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.GET_TEAM_DETAILS)
                .newBuilder()
                .addQueryParameter("uboatName",uboatName )
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                Type type = new TypeToken<List<DTOAllyTeamDetails>>(){}.getType();
                List<DTOAllyTeamDetails> teamsDataList = gson.fromJson(json, type);
                teamsDetailsConsumer.accept(teamsDataList);
            }

        });
    }
}
