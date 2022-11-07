package Uboat;

import DTO.DTODecryptionCandidate;
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

public class DecryptionsCandidatesRefresher extends TimerTask{

    private final Consumer<List<DTODecryptionCandidate>> candidatesConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;
    private String uboatName;

    public DecryptionsCandidatesRefresher(String uboatName,BooleanProperty shouldUpdate, Consumer<List<DTODecryptionCandidate>> candidatesConsumer) {

        this.uboatName = uboatName;
        this.shouldUpdate = shouldUpdate;

        this.candidatesConsumer = candidatesConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {

            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.GET_DECRYPTION_CANDIDATES)
                .newBuilder()
                .addQueryParameter("uboatName", uboatName)
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
                    String json = response.body().string();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<DTODecryptionCandidate>>() {}.getType();
                    List<DTODecryptionCandidate> decryptionCandidateList = gson.fromJson(json, type);
                    candidatesConsumer.accept(decryptionCandidateList);
                }
            }
        });
    }
}
