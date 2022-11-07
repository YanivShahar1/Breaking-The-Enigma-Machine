package Ally;

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
import java.util.TimerTask;
import java.util.function.Consumer;

public class ContestListRefresher extends TimerTask{

    private final Consumer<List<DTOContestData>> contestListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public ContestListRefresher(BooleanProperty shouldUpdate, Consumer<List<DTOContestData>> contestListConsumer) {

        this.shouldUpdate = shouldUpdate;

        //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.contestListConsumer = contestListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {

            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.GET_ALL_CONTESTS_DATA)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfContests = response.body().string();
                Gson gson = new Gson();
                Type type = new TypeToken<List<DTOContestData>>(){}.getType();
                List<DTOContestData> contestDataList = gson.fromJson(jsonArrayOfContests, type);
                contestListConsumer.accept(contestDataList); //check this line..
            }

        });
    }
}
