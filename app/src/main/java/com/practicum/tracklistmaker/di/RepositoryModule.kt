package com.practicum.tracklistmaker.di

import com.practicum.tracklistmaker.mediaLibrary.data.repository.FavouriteTracksRepositoryImpl
import com.practicum.tracklistmaker.mediaLibrary.data.repository.PlaylistsRepositoryImpl
import com.practicum.tracklistmaker.mediaLibrary.domain.repository.FavouriteTracksRepository
import com.practicum.tracklistmaker.mediaLibrary.domain.repository.PlaylistsRepository
import com.practicum.tracklistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.practicum.tracklistmaker.player.domain.repository.AudioPlayerRepository
import com.practicum.tracklistmaker.search.data.repository.TrackSearchRepositoryImpl
import com.practicum.tracklistmaker.search.data.repository.TrackStorageRepositoryImpl
import com.practicum.tracklistmaker.search.domain.repository.TrackSearchRepository
import com.practicum.tracklistmaker.search.domain.repository.TrackStorageRepository
import com.practicum.tracklistmaker.settings.data.repository.SettingsRepositoryImpl
import com.practicum.tracklistmaker.settings.domain.repository.SettingsRepository
import com.practicum.tracklistmaker.sharing.data.repository.NavigatorRepositoryImpl
import com.practicum.tracklistmaker.sharing.domain.externalNavigator.NavigatorRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory <AudioPlayerRepository> {
        AudioPlayerRepositoryImpl()
    }

    single<TrackSearchRepository> {
        TrackSearchRepositoryImpl(networkClient = get(), appDatabase = get())
    }

    single<TrackStorageRepository> {
        TrackStorageRepositoryImpl(trackStorage = get())
    }

    single<NavigatorRepository> {
        NavigatorRepositoryImpl(externalNavigator = get(), context = androidContext())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(appDatabase = get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(context = androidContext(), appDatabase = get())
    }
}



