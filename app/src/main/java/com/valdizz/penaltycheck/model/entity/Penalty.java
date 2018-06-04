package com.valdizz.penaltycheck.model.entity;

import io.realm.RealmObject;

public class Penalty extends RealmObject {
    private String date;
    private String number;
    private boolean checked;

    public Penalty() {
    }

    public Penalty(String date, String number, boolean checked) {
        this.date = date;
        this.number = number;
        this.checked = checked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Penalty penalty = (Penalty) o;
        return number.equals(penalty.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public String toString() {
        return this.getNumber()+"/"+this.getDate()+"/"+this.isChecked();
    }
}
