package Ally;

import DTO.DTOAgentDetails;
import DTO.DTOAllyTeamDetails;
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

public class TeamDetailsRefresher extends TimerTask{

    private final Consumer<List<DTOAgentDetails>> teamsDetailsConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;
    private String allyName;

    public TeamDetailsRefresher(String allyName, BooleanProperty shouldUpdate, Consumer<List<DTOAgentDetails>> teamsDetailsConsumer) {

        this.allyName = allyName;
        this.shouldUpdate = shouldUpdate;
        //System.out.println(shouldUpdate);
        //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
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
                .addQueryParameter("clientType", "ally")
                .addQueryParameter("clientName", allyName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                //System.out.println("json team refresher: " + json);
                Gson gson = new Gson();
                Type type = new TypeToken<List<DTOAgentDetails>>(){}.getType();
                List<DTOAgentDetails> teamsDataList = gson.fromJson(json, type);
                //System.out.println(teamsDataList);
                teamsDetailsConsumer.accept(teamsDataList);
            }

        });
    }
}
