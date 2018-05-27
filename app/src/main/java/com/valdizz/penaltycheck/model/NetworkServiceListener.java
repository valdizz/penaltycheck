package com.valdizz.penaltycheck.model;

import com.valdizz.penaltycheck.model.entity.Auto;

import java.util.Date;
import java.util.List;

public interface NetworkServiceListener {

    void onSuccessRequest(long count);
    void onErrorRequest(String error);
}
