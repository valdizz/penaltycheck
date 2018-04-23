package com.valdizz.penaltycheck.model;

import android.util.Log;

import com.valdizz.penaltycheck.mvp.penaltyactivity.PenaltyActivityContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkService{

    public static final String LOG_TAG_PCHECK = "penaltycheck_log";
    public static final String MVD_URL = "http://mvd.gov.by/Ajax.asmx/GetExt";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String NO_PENALTY_MSG = "По заданным критериям поиска информация не найдена";
    private final OkHttpClient client = new OkHttpClient();

    public void checkPenalty(final PenaltyActivityContract.NetworkServiceListener networkServiceListener, final long id, String fullname, String series, String number) throws Exception {
        JSONObject params = new JSONObject();
        params.put("GuidControl", 2091);
        params.put("Param1", fullname);
        params.put("Param2", series);
        params.put("Param3", number);
        Log.d(LOG_TAG_PCHECK, "Parameters:" + fullname + "/" + series + '/' + number);

        Request request = new Request.Builder()
                .url(MVD_URL)
                .header("Content-Type", "application/json; charset=UTF-8")
                .post(RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String result = response.body().string();
                Log.d(LOG_TAG_PCHECK, "Result:"+result);
                if (!result.contains(NO_PENALTY_MSG)) {
                    RealmService realmService = new RealmService();
                    try {
                        JSONArray penalties = new JSONArray(result);
                        for (int i = 0; i < penalties.length(); i++) {
                            JSONObject penalty = penalties.getJSONObject(i);
                            realmService.addPenalty(id, new Date(), penalty.toString());
                            Log.d(LOG_TAG_PCHECK, "Penalty:" + penalty.toString());
                            networkServiceListener.onFinishedNetworkService(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        realmService.closeRealm();
                        Log.d(LOG_TAG_PCHECK, "Close realm!");
                    }
                }
                else {
                    networkServiceListener.onFinishedNetworkService(false);
                }
            }
        });
    }
}
