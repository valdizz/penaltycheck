package com.valdizz.penaltycheck.db;

import android.util.Log;

import com.valdizz.penaltycheck.model.Auto;
import com.valdizz.penaltycheck.model.Penalty;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

public class DataHelper {

    public static final String AUTOID_PARAM = "auto_id";

    public static void createAuto(final String surname, final String name, final String patronymic, final String series, final String number, final String description, final boolean autocheck) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Number maxValue = realm.where(Auto.class).max("id");
                    long pk = (maxValue != null) ? (long)maxValue + 1 : 0;
                    Auto auto = realm.createObject(Auto.class, pk);
                    auto.setSurname(surname);
                    auto.setName(name);
                    auto.setPatronymic(patronymic);
                    auto.setSeries(series);
                    auto.setNumber(number);
                    auto.setDescription(description);
                    auto.setAutomatically(autocheck);
                }
            });
        } finally {
            realm.close();
        }
    }

    public static void updateAuto(final long id, final String surname, final String name, final String patronymic, final String series, final String number, final String description, final boolean autocheck) {
        Realm realm = Realm.getDefaultInstance();
        try {
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
                }
            });
        } finally {
            realm.close();
        }
    }

    public static void deleteAuto(final long id) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Auto auto = realm.where(Auto.class).equalTo("id", id).findFirst();
                    auto.deleteFromRealm();
                }
            });
        } finally {
            realm.close();
        }
    }

    public static Auto getAuto(long id) {
        Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Auto.class).equalTo("id", id).findFirst();
        } finally {
            realm.close();
        }
    }

    public static boolean checkAuto(long id, String series, String number) {
        Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Auto.class).notEqualTo("id", id).equalTo("series", series).equalTo("number",number).count() > 0;
        } finally {
            realm.close();
        }
    }

    public static OrderedRealmCollection<Auto> getAutos(Realm realm) {
        return realm.where(Auto.class).findAllAsync().sort("id");
    }

    public static OrderedRealmCollection<Penalty> getPenalties(Realm realm, long id) {
        return realm.where(Auto.class).equalTo("id", id).findFirst().getPenalties();
    }

    public static void deletePenalty(final Penalty penalty) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    penalty.deleteFromRealm();
                }
            });
        } finally {
            realm.close();
        }
    }
}
