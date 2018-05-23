package com.valdizz.penaltycheck.dagger;

import com.valdizz.penaltycheck.model.NetworkService;
import com.valdizz.penaltycheck.model.RealmService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {


    @Provides
    RealmService provideRealmService() {
        return new RealmService();
    }

    @Singleton
    @Provides
    NetworkService provideNetworkService() {
        return new NetworkService();
    }
}
