package com.valdizz.penaltycheck.model;

import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.model.entity.Penalty;

import java.util.Date;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

public class RealmService {

    public static final String AUTOID_PARAM = "auto_id";
    public static final String GOOGLEPLAY_URI = "market://details?id=com.valdizz.penaltycheck";

    private final Realm realm;

    public RealmService() {
        realm = Realm.getDefaultInstance();
    }

    public void closeRealm() {
        realm.close();
    }

    public OrderedRealmCollection<Auto> getAutos() {
        return realm.where(Auto.class).findAllAsync().sort("id");
    }

    public List<Auto> getAutos(boolean isAutochecked) {
        List<Auto> autos;
        if (isAutochecked)
            autos = realm.where(Auto.class).equalTo("automatically", true).findAll().sort("id");
        else
            autos = realm.where(Auto.class).findAllAsync().sort("id");
        return realm.copyFromRealm(autos);
    }

    public Auto getAuto(long id) {
        Auto auto = realm.where(Auto.class).equalTo("id", id).findFirst();
        return realm.copyFromRealm(auto);
    }

    public void addAuto(final String surname, final String name, final String patronymic, final String series, final String number, final String description, final boolean autocheck, final byte[] image) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxValue = realm.where(Auto.class).max("id");
                long pk = (maxValue != null) ? (long) maxValue + 1 : 1;
                Auto auto = realm.createObject(Auto.class, pk);
                auto.setSurname(surname);
                auto.setName(name);
                auto.setPatronymic(patronymic);
                auto.setSeries(series);
                auto.setNumber(number);
                auto.setDescription(description);
                auto.setAutomatically(autocheck);
                auto.setImage(image);
            }
        });
    }

    public void updateAuto(final long id, final String surname, final String name, final String patronymic, final String series, final String number, final String description, final boolean autocheck, final byte[] image) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Auto auto = realm.where(Auto.class).equalTo("id", id).findFirst();
                auto.setSurname(surname);
                auto.setName(name);
                auto.setPatronymic(patronymic);
                auto.setSeries(series);
                auto.setNumber(number);
                auto.setDescription(description);
                auto.setAutomatically(autocheck);
                auto.setImage(image);
            }
        });
    }

    public void updateLastCheckDateAuto(final long id, final Date lastupdate) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Auto auto = realm.where(Auto.class).equalTo("id", id).findFirst();
                auto.setLastupdate(lastupdate);
            }
        });
    }

    public void deleteAuto(final long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Auto auto = realm.where(Auto.class).equalTo("id", id).findFirst();
                auto.deleteFromRealm();
            }
        });
    }

    public boolean checkAuto(long id, String series, String number) {
        return realm.where(Auto.class).notEqualTo("id", id).equalTo("series", series).equalTo("number", number).count() > 0;
    }

    public OrderedRealmCollection<Penalty> getPenalties(long id) {
        return realm.where(Auto.class).equalTo("id", id).findFirst().getPenalties();
    }

    public void addPenalty(final long id, final String date, final String number) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Penalty penalty = realm.createObject(Penalty.class);
                penalty.setDate(date);
                penalty.setNumber(number);
                if (!realm.where(Auto.class).equalTo("id", id).findFirst().getPenalties().contains(penalty))
                    realm.where(Auto.class).equalTo("id", id).findFirst().getPenalties().add(penalty);
            }
        });
    }

    public void deletePenalty(final Penalty penalty) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                penalty.deleteFromRealm();
            }
        });
    }

    public void viewPenalty(final Penalty penalty) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                penalty.setChecked(true);
            }
        });
    }
}
