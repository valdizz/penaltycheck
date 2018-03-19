package com.valdizz.penaltycheck.dagger;

import com.valdizz.penaltycheck.model.RealmService;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Provides
    RealmService provideRealmService() {
        return new RealmService();
    }
}
