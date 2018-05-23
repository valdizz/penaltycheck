package com.valdizz.penaltycheck.model.entity;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;

public class Penalty extends RealmObject {
    private Date date;
    private String number;
    private boolean checked;

    public Penalty() {
    }

    public Penalty(Date date, String number, boolean checked) {
        this.date = date;
        this.number = number;
        this.checked = checked;
    }

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

    @Override
    public String toString() {
        return this.getNumber()+"/"+this.getDate()+"/"+this.isChecked();
    }
}
