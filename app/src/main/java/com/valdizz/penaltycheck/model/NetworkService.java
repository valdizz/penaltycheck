package com.valdizz.penaltycheck.model;

import android.util.Log;

import com.valdizz.penaltycheck.model.entity.Auto;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkService{

    private static final String LOG_TAG_PCHECK = "penaltycheck_log";
    private static final String MVD_URL = "http://mvd.gov.by/Ajax.asmx/GetExt";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String NO_PENALTY_MSG = "По заданным критериям поиска информация не найдена";
    private final OkHttpClient client = new OkHttpClient();

    private Request getRequest(Auto auto) throws Exception{
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
        return Observable.fromCallable(() -> client.newCall(getRequest(auto)).execute());
    }

    public Disposable checkPenalty(NetworkServiceListener networkServiceListener, List<Auto> autos) {
        return Observable
                .fromIterable(autos)
                .flatMap(auto -> makeRequest(auto)
                        .subscribeOn(Schedulers.io())
                        .filter(Response::isSuccessful)
                        .map(response -> response.body().string())
                        .doOnNext(response_string -> {
                            Log.d(LOG_TAG_PCHECK, "Save date: " + response_string + " / " + Thread.currentThread().getName());
                            saveLastCheckDate(auto);
                        })
                        //.filter(response_string -> !response_string.contains(NO_PENALTY_MSG))
                        .doOnNext(response_string -> {
                            Log.d(LOG_TAG_PCHECK, "Found penalty: " + response_string + " / " + Thread.currentThread().getName());
                            //parseAndSavePenalties(response_string, networkServiceListener, auto);
                            RealmService realmService = new RealmService();
                            realmService.addPenalty(auto.getId(), new Date().toString(), response_string);
                            realmService.closeRealm();
                        })
                )
                .count()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(Long count) {
                        Log.d(LOG_TAG_PCHECK, "Request complete: " + count);
                        networkServiceListener.onSuccessRequest(count);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG_PCHECK, "Request error: " + e.getLocalizedMessage());
                        networkServiceListener.onErrorRequest(e.getLocalizedMessage());
                    }
                });
    }

    private void saveLastCheckDate(Auto auto){
        RealmService realmService = new RealmService();
        realmService.updateLastCheckDateAuto(auto.getId(), new Date());
        realmService.closeRealm();
    }

    private void parseAndSavePenalties(String response, NetworkServiceListener networkServiceListener, Auto auto){
        RealmService realmService = new RealmService();
        Document table = Jsoup.parse(response);
        Elements rows = table.select("tr");
        for (int i = 1; i < rows.size(); i++){
            Elements cols = rows.get(i).select("td");
            realmService.addPenalty(auto.getId(), cols.get(3).text(), cols.get(4).text());
        }
        realmService.closeRealm();
    }
}
