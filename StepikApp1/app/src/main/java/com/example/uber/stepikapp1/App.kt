package com.example.uber.stepikapp1

import android.annotation.SuppressLint
import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration




class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val realmConfig = RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(realmConfig)

    }
}