package com.valdizz.penaltycheck.model.entity;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Auto extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;
    private String surname;
    private String patronymic;
    private String series;
    private String number;
    private String description;
    private boolean automatically;
    private byte[] image;
    private Date lastupdate;
    private RealmList<Penalty> penalties;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAutomatically() {
        return automatically;
    }

    public void setAutomatically(boolean automatically) {
        this.automatically = automatically;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Date lastupdate) {
        this.lastupdate = lastupdate;
    }

    public RealmList<Penalty> getPenalties() {
        return penalties;
    }

    public void setPenalties(RealmList<Penalty> penalties) {
        this.penalties = penalties;
    }

    public String getFullName() {
        return surname + " " + name + " " + patronymic;
    }

    public String getFullDoc() {
        return series + " " + number;
    }

}
