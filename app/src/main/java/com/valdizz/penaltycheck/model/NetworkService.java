package com.valdizz.penaltycheck.model;

import android.util.Log;

import com.valdizz.penaltycheck.PenaltyCheckApplication;
import com.valdizz.penaltycheck.model.entity.Auto;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkService {

    private static final String MVD_URL = "http://mvd.gov.by/Ajax.asmx/GetExt";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String NO_PENALTY_MSG = "По заданным критериям поиска информация не найдена";
    private final OkHttpClient client = new OkHttpClient();


    private Request getRequest(Auto auto) throws Exception {
        JSONObject params = new JSONObject();
        params.put("GuidControl", 2091);
        params.put("Param1", auto.getFullName());
        params.put("Param2", auto.getSeries());
        params.put("Param3", auto.getNumber());
        return new Request.Builder()
                .url(MVD_URL)
                .header("Content-Type", "application/json; charset=UTF-8")
                .post(RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
                .build();
    }

    private Observable<Response> makeRequest(final Auto auto) {
        return Observable.create(emitter -> {
            Call call = client.newCall(getRequest(auto));
            emitter.setCancellable(call::cancel);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    Log.d(PenaltyCheckApplication.TAG, "Request complete: " + response.toString());
                    emitter.onNext(response);
                    emitter.onComplete();
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(PenaltyCheckApplication.TAG, "Request error: " + e.getLocalizedMessage());
                    emitter.tryOnError(e);
                }
            });
        });
    }

    public Disposable checkPenalty(NetworkServiceListener networkServiceListener, List<Auto> autos) {
        return Observable
                .fromIterable(autos)
                .flatMap(auto -> makeRequest(auto)
                        .filter(Response::isSuccessful)
                        .map(response -> response.body().string())
                        .doOnNext(responseString -> {
                            Log.d(PenaltyCheckApplication.TAG, "Request (save date): " + responseString);
                            saveLastCheckDate(auto);
                        })
                        .filter(responseString -> !responseString.contains(NO_PENALTY_MSG))
                        .doOnNext(responseString -> {
                            Log.d(PenaltyCheckApplication.TAG, "Request (found penalty): " + responseString);
                            parseAndSavePenalties(responseString, auto);
                        })
                )
                .count()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(Long count) {
                        long newPenalties = 0;
                        if (count > 0) {
                            RealmService realmService = new RealmService();
                            for (Auto auto : autos) {
                                newPenalties = newPenalties + realmService.getAuto(auto.getId()).getNewPenalties();
                            }
                            realmService.closeRealm();
                        }
                        Log.d(PenaltyCheckApplication.TAG, "All requests is complete: " + count + "/" + newPenalties);
                        networkServiceListener.onSuccessRequest(newPenalties);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(PenaltyCheckApplication.TAG, "All requests complete with error: " + e.getLocalizedMessage());
                        networkServiceListener.onErrorRequest(e.getLocalizedMessage());
                    }
                });
    }

    private void saveLastCheckDate(Auto auto) {
        RealmService realmService = new RealmService();
        realmService.updateLastCheckDateAuto(auto.getId(), new Date());
        realmService.closeRealm();
    }

    private void parseAndSavePenalties(String response, Auto auto) {
        RealmService realmService = new RealmService();
        String response_html = StringEscapeUtils.unescapeJava(response);
        Document doc = Jsoup.parse(response_html);
        Element table = doc.select("table").first();
        Elements rows = table.select("tr");
        for (int i = 1; i < rows.size(); i++) {
            Elements cols = rows.get(i).select("td");
            realmService.addPenalty(auto.getId(), cols.get(3).text(), cols.get(4).text());
        }
        realmService.closeRealm();
    }
}
