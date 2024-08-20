package com.practicum.tracklistmaker.di

import com.practicum.tracklistmaker.mediaLibrary.domain.interactor.FavouriteTracksInteractor
import com.practicum.tracklistmaker.mediaLibrary.domain.interactor.PlaylistInteractor
import com.practicum.tracklistmaker.player.domain.interactor.AudioPlayerInteractor
import com.practicum.tracklistmaker.search.domain.interactor.GetHistoryTrackUseCase
import com.practicum.tracklistmaker.search.domain.interactor.SaveHistoryTrackUseCase
import com.practicum.tracklistmaker.search.domain.interactor.SearchTrackUseCase
import com.practicum.tracklistmaker.settings.domain.interactor.SettingsInteractor
import com.practicum.tracklistmaker.sharing.interactor.NavigatorInteractor
import org.koin.dsl.module

val interactorModule = module {
    factory {
        AudioPlayerInteractor(audioPlayerRepository = get())
    }

    factory {
        SearchTrackUseCase(trackSearchRepository = get())
    }

    factory {
        SaveHistoryTrackUseCase(trackStorage = get())
    }

    factory {
        GetHistoryTrackUseCase(trackStorage = get())
    }

    factory {
        NavigatorInteractor(navigatorRepository = get())
    }

    factory {
        SettingsInteractor(settingsRepository = get())
    }

    factory {
        FavouriteTracksInteractor(favouriteTracksRepository = get())
    }

    factory {
        PlaylistInteractor(playlistsRepository = get())
    }
}
