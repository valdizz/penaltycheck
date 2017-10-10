package com.valdizz.penaltycheck.db;

import android.util.Log;

import com.valdizz.penaltycheck.model.Auto;

import io.realm.Realm;

public class DataHelper {

    public static void createAuto(final Auto auto) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Number maxValue = realm.where(Auto.class).max("id");
                    long pk = (maxValue != null) ? (long)maxValue + 1 : 0;
                    auto.setId(pk);
                    realm.insert(auto);
                    Log.d("ddd create","id="+auto.getId());
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

    public static void updateAuto(final Auto auto) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(auto);
                    Log.d("ddd update","id="+auto.getId());
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
}
