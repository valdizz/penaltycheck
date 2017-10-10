package com.valdizz.penaltycheck.model;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;

public class Penalty extends RealmObject {
    private Date date;
    private String number;
    private boolean checked;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
