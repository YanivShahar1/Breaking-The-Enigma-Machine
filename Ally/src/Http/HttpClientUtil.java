package Http;

import java.io.File;


import okhttp3.*;
public class HttpClientUtil {
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .followRedirects(false)
                    .build();
    private final static int METHOD = 0;
    private final static int FILE_PATH = 1;
    public static void runAsync(String finalUrl, Callback callback, String... strings) {
        Request request;
        if (strings.length > 0 && strings[METHOD].equals("loadXMLFileButton")) {
            File file = new File(strings[FILE_PATH]);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("xml"), file))
                    .build();
            request = new Request.Builder()
                    .url(finalUrl)
                    .post(body)
                    .build();
        }
        else {
            request = new Request.Builder()
                .url(finalUrl)
                .build();
        }

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}