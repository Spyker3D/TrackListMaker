package com.practicum.tracklistmaker.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.tracklistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.tracklistmaker.search.data.network.ITUNES_URL
import com.practicum.tracklistmaker.search.data.network.ItunesApiService
import com.practicum.tracklistmaker.search.data.network.NetworkClient
import com.practicum.tracklistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.tracklistmaker.search.data.storage.SharedPrefsStorage
import com.practicum.tracklistmaker.search.data.storage.TrackStorage
import com.practicum.tracklistmaker.sharing.data.externalNavigator.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PLAYLISTMAKER_SHARED_PREFS = "com.practicum.playlistmaker.MY_PREFS"

val dataModule = module {
    single<NetworkClient> {
        RetrofitNetworkClient(context = androidContext(), iTunesApiService = get())
    }

    single<TrackStorage> {
        SharedPrefsStorage(sharedPreferences = get(), gson = get())
    }

    single {
        ExternalNavigator(context = androidContext())
    }

    single {
        androidContext().getSharedPreferences(PLAYLISTMAKER_SHARED_PREFS, Context.MODE_PRIVATE)
    }

    single<ItunesApiService> {
        Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApiService::class.java)
    }

    factory { Gson() }

    single<AppDatabase> {
        AppDatabase.getInstance(androidContext())
    }
}
